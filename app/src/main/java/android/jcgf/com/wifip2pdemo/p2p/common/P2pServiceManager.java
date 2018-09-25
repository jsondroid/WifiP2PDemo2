package android.jcgf.com.wifip2pdemo.p2p.common;

import android.jcgf.com.wifip2pdemo.p2p.listen.IOnP2pServiceListener;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pConnetListener;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pScanListener;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public class P2pServiceManager {
    private static P2pServiceManager instance;

    private Handler handler=new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if (onP2pScanListener != null) {
                        onP2pScanListener.onPeersAvailable(null);
                    }
                    if (onP2pScanListeners != null) {
                        ArrayList<OnP2pScanListener> scanListeners = onP2pScanListeners;
                        for (OnP2pScanListener scanListener : scanListeners) {
                            if (scanListener != null) {
                                scanListener.onPeersAvailable(null);
                            }
                        }
                    }
                    stopScanPeers(null);
                }
            });
        }
    };

    public static P2pServiceManager getInstance() {
        if (instance == null) {
            synchronized (P2pServiceManager.class) {
                if (instance == null) {
                    instance = new P2pServiceManager();
                }
            }
        }
        return instance;
    }

    private WifiP2pService service;

    public P2pServiceManager setService(IBinder iBinder) {
        service = ((WifiP2pService.LocalBinder) iBinder).getWifiP2pService(iOnP2pServiceListener);
        service.initP2p();
        return instance;
    }

    public void createGroup(WifiP2pManager.ActionListener actionListener) {
        if (service != null) {
            service.createGroup(actionListener);
        }
    }

    public void removeGroup(WifiP2pManager.ActionListener actionListener) {
        if (service != null) {
            service.removeGroup(actionListener);
        }
    }

    public void scanPeers(WifiP2pManager.ActionListener actionListener) {
        if (service != null) {
            handler.removeCallbacksAndMessages(null);
            service.scanPeers(actionListener);
            handler.postDelayed(runnable,3*1000);
        }
    }

    public void stopScanPeers(WifiP2pManager.ActionListener actionListener) {
        if (service != null) {
            service.stopScanPeers(actionListener);
        }
    }

    public void connectP2pDevice(WifiP2pDevice wifiP2pDevice, WifiP2pManager.ActionListener actionListener) {
        if (service != null) {
            service.connectP2pDevice(wifiP2pDevice, actionListener);
        }
    }

    public void disConnetP2pDevice(WifiP2pManager.ActionListener actionListener) {
        if (service != null) {
            service.disConnetP2pDevice(actionListener);
        }
    }

    public void requestGroupInfo(WifiP2pManager.GroupInfoListener groupInfoListener) {
        if (service != null) {
            service.requestGroupInfo(groupInfoListener);
        }
    }

    public String getThisName() {
        if (service != null) {
            return service.getThisName();
        }
        return null;
    }

    public WifiP2pDevice getThisP2pDevice() {
        if (service != null) {
            return service.getThisP2pDevice();
        }
        return null;
    }

    public WifiP2pDevice getConnetDevice() {
        if (service != null) {
            return service.getConnetDevice();
        }
        return null;
    }

    public WifiP2pInfo getWifiP2pInfo() {
        if (service != null) {
            return service.getWifiP2pInfo();
        }
        return null;
    }


    private OnP2pConnetListener onP2pConnetListener;
    private OnP2pScanListener onP2pScanListener;

    private ArrayList<OnP2pConnetListener> onP2pConnetListeners;
    private ArrayList<OnP2pScanListener> onP2pScanListeners;

    public void setOnP2pConnetListener(OnP2pConnetListener onP2pConnetListener) {
        this.onP2pConnetListener = onP2pConnetListener;
    }

    public void setOnP2pScanListener(OnP2pScanListener onP2pScanListener) {
        this.onP2pScanListener = onP2pScanListener;
    }

    public void addOnP2pConnetListeners(OnP2pConnetListener onP2pConnetListener) {
        if (this.onP2pConnetListeners == null) {
            this.onP2pConnetListeners = new ArrayList<>();
        }
        if (!this.onP2pConnetListeners.contains(onP2pConnetListener)) {
            this.onP2pConnetListeners.add(onP2pConnetListener);
        }
    }

    public void addOnP2pScanListeners(OnP2pScanListener onP2pScanListener) {
        if (this.onP2pScanListeners == null) {
            this.onP2pScanListeners = new ArrayList<>();
        }
        if (!this.onP2pScanListeners.contains(onP2pScanListener)) {
            this.onP2pScanListeners.add(onP2pScanListener);
        }
    }


    private IOnP2pServiceListener iOnP2pServiceListener = new IOnP2pServiceListener() {
        @Override
        public void onWifiP2pEnabled(final boolean isenable) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if (onP2pConnetListener != null) {
                        onP2pConnetListener.onWifiP2pEnabled(isenable);
                    }
                    if (onP2pScanListener != null) {
                        onP2pScanListener.onWifiP2pEnabled(isenable);
                    }
                    if (onP2pConnetListeners != null) {
                        ArrayList<OnP2pConnetListener> connetListeners = onP2pConnetListeners;
                        for (OnP2pConnetListener connet : connetListeners) {
                            if (connet != null) {
                                connet.onWifiP2pEnabled(isenable);
                            }
                        }
                    }
                    if (onP2pScanListeners != null) {
                        ArrayList<OnP2pScanListener> scanListeners = onP2pScanListeners;
                        for (OnP2pScanListener scanListener : scanListeners) {
                            if (scanListener != null) {
                                scanListener.onWifiP2pEnabled(isenable);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void onPeersAvailable(final WifiP2pDeviceList peers) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    handler.removeCallbacksAndMessages(null);

                    if (onP2pConnetListener != null) {
                        onP2pConnetListener.onPeersAvailable(peers);
                    }
                    if (onP2pConnetListeners != null) {
                        ArrayList<OnP2pConnetListener> connetListeners = onP2pConnetListeners;
                        for (OnP2pConnetListener connet : connetListeners) {
                            if (connet != null) {
                                connet.onPeersAvailable(peers);
                            }
                        }
                    }
                    if (onP2pScanListener != null) {
                        onP2pScanListener.onPeersAvailable(peers);
                    }
                    if (onP2pScanListeners != null) {
                        ArrayList<OnP2pScanListener> scanListeners = onP2pScanListeners;
                        for (OnP2pScanListener scanListener : scanListeners) {
                            if (scanListener != null) {
                                scanListener.onPeersAvailable(peers);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void onConnection(final WifiP2pInfo info) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if (onP2pConnetListener != null) {
                        onP2pConnetListener.onConnection(info);
                    }
                    if (onP2pConnetListeners != null) {
                        ArrayList<OnP2pConnetListener> connetListeners = onP2pConnetListeners;
                        for (OnP2pConnetListener connet : connetListeners) {
                            if (connet != null) {
                                connet.onConnection(info);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void onDisConnection() {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if (onP2pConnetListener != null) {
                        onP2pConnetListener.onDisConnection();
                    }
                    if (onP2pConnetListeners != null) {
                        ArrayList<OnP2pConnetListener> connetListeners = onP2pConnetListeners;
                        for (OnP2pConnetListener connet : connetListeners) {
                            if (connet != null) {
                                connet.onDisConnection();
                            }
                        }
                    }
                }
            });
        }

        @Override
        public void onThisDeviceInfo(final WifiP2pDevice device) {
            MainHandler.getInstance().runInMainThread(new Runnable() {
                @Override
                public void run() {
                    if (onP2pConnetListener != null) {
                        onP2pConnetListener.onThisName(device);
                    }
                    if (onP2pConnetListeners != null) {
                        ArrayList<OnP2pConnetListener> connetListeners = onP2pConnetListeners;
                        for (OnP2pConnetListener connet : connetListeners) {
                            if (connet != null) {
                                connet.onThisName(device);
                            }
                        }
                    }
                }
            });
        }
    };
}
