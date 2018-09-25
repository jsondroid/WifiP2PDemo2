package android.jcgf.com.wifip2pdemo.p2p.common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.jcgf.com.wifip2pdemo.p2p.listen.IOnP2pServiceListener;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pListener;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public class WifiP2pService extends Service implements OnP2pListener {
    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public WifiP2pService getWifiP2pService(IOnP2pServiceListener iOnP2pServiceListener) {
            WifiP2pService.this.iOnP2pServiceListener = iOnP2pServiceListener;
            return WifiP2pService.this;
        }
    }

    private IOnP2pServiceListener iOnP2pServiceListener;
    private WifiP2pService.LocalBinder binder = new WifiP2pService.LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    private WifiP2pManager manager;
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private WifiP2PBrodcastRecever receiver = null;
    private boolean isreg = false;

    /**
     * 当前连接的设备
     */
    private WifiP2pDevice connetDevice;

    /**
     * 初始化p2p
     */
    public void initP2p() {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        String name=android.os.Build.MODEL;
        setDeviceName(name);
        receiver = new WifiP2PBrodcastRecever(manager, channel, this);
        regReceiver();

    }

    /**
     * 注册广播
     */
    public void regReceiver() {
        if (!isreg && receiver != null) {
            registerReceiver(receiver, intentFilter);
            isreg = true;
        }
    }

    public void unregReceiver() {
        if (isreg && receiver != null) {
            unregisterReceiver(receiver);
            isreg = false;
        }
    }

    public void scanPeers(WifiP2pManager.ActionListener actionListener) {
        if (manager != null) {
            manager.discoverPeers(channel, actionListener);
        }
    }

    public void stopScanPeers(WifiP2pManager.ActionListener actionListener) {
        if (manager != null) {
            manager.stopPeerDiscovery(channel, actionListener);
        }
    }


    /**
     * 连接p2p
     */
    public void connectP2pDevice(WifiP2pDevice wifiP2pDevice, WifiP2pManager.ActionListener actionListener) {
        connetDevice = wifiP2pDevice;
        if (manager != null) {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = wifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            connectP2pDevice(config, actionListener);
        }
    }

    private void connectP2pDevice(WifiP2pConfig config, WifiP2pManager.ActionListener actionListener) {
        if (manager != null) {
            if (actionListener == null) {
                manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
            } else
                manager.connect(channel, config, actionListener);
        }
    }

    /**
     * 断开连接
     */
    public void disConnetP2pDevice(WifiP2pManager.ActionListener actionListener) {
        wifiP2pInfo = null;
        if (manager != null) {
            if (actionListener == null) {
                manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
            } else
                manager.cancelConnect(channel, actionListener);
            removeGroup(actionListener);
        }
    }

    public void createGroup(WifiP2pManager.ActionListener actionListener) {
        if (manager != null) {
            if (actionListener == null) {
                manager.createGroup(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
            } else
                manager.createGroup(channel, actionListener);
        }
    }

    public void removeGroup(WifiP2pManager.ActionListener actionListener) {
        if (manager != null) {
            if (actionListener == null) {
                manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });
            } else
                manager.removeGroup(channel, actionListener);
        }
    }

    public void requestGroupInfo(WifiP2pManager.GroupInfoListener groupInfoListener) {
        if (manager != null) {
            manager.requestGroupInfo(channel, groupInfoListener);
        }
    }

    /**
     * p2p是否可用
     */
    private boolean isp2penable = false;

    public boolean isP2pEnabled() {
        return isp2penable;
    }

    /**
     * 本机设备名称
     */
    private String thisName = "";

    public String getThisName() {
        return thisName;
    }

    private WifiP2pDevice thisP2pDevice;

    public WifiP2pDevice getThisP2pDevice() {
        return thisP2pDevice;
    }

    private WifiP2pInfo wifiP2pInfo;

    public WifiP2pInfo getWifiP2pInfo() {
        return wifiP2pInfo;
    }

    public WifiP2pDevice getConnetDevice() {
        return connetDevice;
    }

    /**
     * p2p回调
     */
    @Override
    public void onWifiP2pEnabled(boolean isenable) {
        isp2penable = isenable;
        if (iOnP2pServiceListener != null) {
            iOnP2pServiceListener.onWifiP2pEnabled(isenable);
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        if (iOnP2pServiceListener != null) {
            iOnP2pServiceListener.onPeersAvailable(peers);
        }
    }

    @Override
    public void onConnection(WifiP2pInfo info) {
        wifiP2pInfo = info;
        if (iOnP2pServiceListener != null) {
            iOnP2pServiceListener.onConnection(info);
        }
    }

    @Override
    public void onDisConnection() {
        wifiP2pInfo = null;
        if (iOnP2pServiceListener != null) {
            iOnP2pServiceListener.onDisConnection();
        }
    }

    @Override
    public void onThisDeviceInfo(WifiP2pDevice device) {
        thisName = device.deviceName;
        thisP2pDevice = device;
        if (iOnP2pServiceListener != null) {
            iOnP2pServiceListener.onThisDeviceInfo(device);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregReceiver();
        disConnetP2pDevice(new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }


    public void reNameP2p() {
        try {
            Method method = manager.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class);
            method.invoke(manager, channel, "androidp2pTest", new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.i("name----", "name success");
                }

                @Override
                public void onFailure(int reason) {
                    Log.i("name----", "name success");
                }
            });

        } catch (Exception e) {
            Log.i("name----", "Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setDeviceName(String devName) {
        try {
            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = manager.getClass().getMethod(
                    "setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);

            Object arglist[] = new Object[3];
            arglist[0] = channel;
            arglist[1] = devName;
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                   Log.e("setDeviceName succeeded","setDeviceName");
                }

                @Override
                public void onFailure(int reason) {
                    Log.e("setDeviceName onFailure","setDeviceName");
                }
            };
            setDeviceName.invoke(manager, arglist);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
