package android.jcgf.com.wifip2pdemo.p2p.listen;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public interface IOnP2pServiceListener {
    public void onWifiP2pEnabled(boolean isenable);//是否可用
    public void onPeersAvailable(WifiP2pDeviceList peers);//获取搜索可用的p2p
    public void onConnection(WifiP2pInfo info);
    public void onDisConnection();
    public void onThisDeviceInfo(WifiP2pDevice device);
}
