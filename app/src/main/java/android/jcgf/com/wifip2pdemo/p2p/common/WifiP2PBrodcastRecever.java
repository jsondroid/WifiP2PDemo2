package android.jcgf.com.wifip2pdemo.p2p.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pListener;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public class WifiP2PBrodcastRecever extends BroadcastReceiver {

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private OnP2pListener onP2pListener;

    public WifiP2PBrodcastRecever(WifiP2pManager manager, WifiP2pManager.Channel channel, OnP2pListener onP2pListener) {
        this.manager = manager;
        this.channel = channel;
        this.onP2pListener = onP2pListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("广播状态---",""+action);
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // UI update to indicate wifi p2p status.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            //WiFi P2P是否可用
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                if (onP2pListener != null) {
                    onP2pListener.onWifiP2pEnabled(true);
                }
            } else {
                if (onP2pListener != null) {
                    onP2pListener.onWifiP2pEnabled(false);
                }
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //P2P peers changed(搜索到可连接的p2p)
            if (manager != null) {
                manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        if (onP2pListener != null) {
                            onP2pListener.onPeersAvailable(peers);
                        }
                    }
                });
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            /**连接p2p变化*/
            if (manager == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                /**已连接的信息*/
                manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        if (onP2pListener != null) {
                            onP2pListener.onConnection(info);
                        }
                    }
                });
            } else {
             /*  连接断开*/
                if (onP2pListener != null) {
                    onP2pListener.onDisConnection();
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            if (onP2pListener != null) {
                onP2pListener.onThisDeviceInfo(wifiP2pDevice);
            }
        }
    }
}
