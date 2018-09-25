package android.jcgf.com.wifip2pdemo.p2p.listen;

import android.net.wifi.p2p.WifiP2pDeviceList;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public interface OnP2pScanListener {
    public void onWifiP2pEnabled(boolean isenable);//是否可用
    public void onPeersAvailable(WifiP2pDeviceList peers);//获取搜索可用的p2p
}
