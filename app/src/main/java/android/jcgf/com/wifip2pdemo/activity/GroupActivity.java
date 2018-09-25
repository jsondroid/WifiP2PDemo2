package android.jcgf.com.wifip2pdemo.activity;

import android.content.Intent;
import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.p2p.common.P2pServiceManager;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pConnetListener;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity implements OnP2pConnetListener {

    TextView info_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        info_tv = findViewById(R.id.info_tv);

        P2pServiceManager.getInstance().setOnP2pConnetListener(this);

        info_tv.postDelayed(new Runnable() {
            @Override
            public void run() {
                P2pServiceManager.getInstance().requestGroupInfo(new WifiP2pManager.GroupInfoListener() {
                    @Override
                    public void onGroupInfoAvailable(WifiP2pGroup info) {
                        setstr(info);
                    }
                });
            }
        }, 500);

    }


    public void openUDP(View view) {
        WifiP2pInfo p2pInfo = P2pServiceManager.getInstance().getWifiP2pInfo();
        if (p2pInfo != null) {
            String string = p2pInfo.groupOwnerAddress.getHostAddress();
            if (!TextUtils.isEmpty(string)) {
                Intent intent = new Intent(this, UDPActivity.class);
                intent.putExtra("ip", string);
                intent.putExtra("ips", ips);
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        }

    }

    public void openSocket(View view) {
        WifiP2pInfo p2pInfo = P2pServiceManager.getInstance().getWifiP2pInfo();
        if (p2pInfo != null) {
            String string = p2pInfo.groupOwnerAddress.getHostAddress();
            if (!TextUtils.isEmpty(string)) {
                Intent intent = new Intent(this, SocketActivity.class);
                intent.putExtra("ip", string);
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        P2pServiceManager.getInstance().removeGroup(null);
    }

    @Override
    public void onWifiP2pEnabled(boolean isenable) {

    }

    @Override
    public void onConnection(WifiP2pInfo info) {

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Log.e("onPeersAvailable--->", "" + peers.toString());
        P2pServiceManager.getInstance().requestGroupInfo(new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup info) {
                setstr(info);
            }
        });
    }

    @Override
    public void onDisConnection() {

    }

    @Override
    public void onThisName(WifiP2pDevice device) {
        info_tv.setText("组信息：\n" + device.toString() + "\n\n\n当前组客户端：\n");
        if (stringBuilder != null) {
            info_tv.append("-------------------------------\n\n" + stringBuilder.toString() + "\n\n-------------------------------\n");
        }

    }

    private ArrayList<String> ips = new ArrayList<>();
    private StringBuilder stringBuilder;

    private void setstr(WifiP2pGroup info) {
        if (info != null && info.getClientList() != null) {
            ips.clear();
            stringBuilder = new StringBuilder();
            for (WifiP2pDevice device : info.getClientList()) {
                stringBuilder.append(device.toString());
                ips.add(device.deviceAddress);
            }
            info_tv.append("组信息：\n" +"");
        }

    }
}
