package android.jcgf.com.wifip2pdemo.activity;

import android.content.Intent;
import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.p2p.common.P2pServiceManager;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pConnetListener;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ConnetActivity extends AppCompatActivity implements OnP2pConnetListener {

    private TextView tv_info;
    private TextView sratus;
    private TextView connet_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connet);
        initview();
        initData();
    }

    private void initview() {
        tv_info = findViewById(R.id.tv_info);
        sratus = findViewById(R.id.sratus);
        connet_tv = findViewById(R.id.connet_tv);

        P2pServiceManager.getInstance().addOnP2pConnetListeners(this);
    }

    private WifiP2pDevice wifiP2pDevice;

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            wifiP2pDevice = bundle.getParcelable("device");
            if (wifiP2pDevice != null) {
                tv_info.setText(wifiP2pDevice.toString());
                WifiP2pDevice device = P2pServiceManager.getInstance().getConnetDevice();
                if (device != null && wifiP2pDevice.deviceAddress.equals(device.deviceAddress)) {
                    sratus.setText((wifiP2pDevice.status == WifiP2pDevice.CONNECTED ? "已连接" : "未连接"));
                    WifiP2pInfo p2pInfo = P2pServiceManager.getInstance().getWifiP2pInfo();
                    setinfo(p2pInfo == null ? "" : p2pInfo.toString());
                }
            }
        }
    }

    public void onconnet(View view) {
        P2pServiceManager.getInstance().connectP2pDevice(wifiP2pDevice, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ConnetActivity.this, "正在连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(ConnetActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void disonconnet(View view) {
        sratus.setText("未连接");
        connet_tv.setText("");
        P2pServiceManager.getInstance().disConnetP2pDevice(new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                finish();
                Toast.makeText(ConnetActivity.this, "断开成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(ConnetActivity.this, "断开失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onWifiP2pEnabled(boolean isenable) {

    }

    @Override
    public void onConnection(WifiP2pInfo info) {
        sratus.setText("已连接");
        setinfo(info.toString());
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }

    @Override
    public void onDisConnection() {
        sratus.setText("未连接");
    }

    @Override
    public void onThisName(WifiP2pDevice device) {

    }

    private void setinfo(String info) {
        connet_tv.setText("\n连接信息：" + info);
    }


    private static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }


    public void openUDPservice(View view) {
        WifiP2pInfo info = P2pServiceManager.getInstance().getWifiP2pInfo();
        if (info != null) {
            String address = info.groupOwnerAddress.getHostAddress();
            Intent intent = new Intent(this, UDPActivity.class);
            intent.putExtra("ip", address);
            intent.putExtra("type", 3);
            startActivity(intent);
        }
    }

    public void openSocket(View view) {
        WifiP2pInfo info = P2pServiceManager.getInstance().getWifiP2pInfo();
        if (info != null) {
            String address = info.groupOwnerAddress.getHostAddress();
            Intent intent = new Intent(this, SocketActivity.class);
            intent.putExtra("ip", address);
            intent.putExtra("type", 3);
            startActivity(intent);
        }
    }

    public void openRTSP(View view) {
        WifiP2pInfo info = P2pServiceManager.getInstance().getWifiP2pInfo();
        if (info != null) {
            String address = info.groupOwnerAddress.getHostAddress();
            Intent intent = new Intent(this, RtspActivity.class);
            intent.putExtra("ip", address);
            intent.putExtra("type", 3);
            startActivity(intent);
        }
    }


}
