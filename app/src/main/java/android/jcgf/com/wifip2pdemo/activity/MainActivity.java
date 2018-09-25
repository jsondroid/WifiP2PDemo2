package android.jcgf.com.wifip2pdemo.activity;

import android.content.Intent;
import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.p2p.common.P2pServiceManager;
import android.jcgf.com.wifip2pdemo.p2p.listen.OnP2pScanListener;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnP2pScanListener {
    private SwipeRefreshLayout swiplayout;

    private ListView listView;
    private ItemAdapter adapter;
    private List<WifiP2pDevice> p2pDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initdata();

        P2pServiceManager.getInstance().addOnP2pScanListeners(this);
    }


    private void initView() {
        swiplayout = findViewById(R.id.swiplayout);
        listView = findViewById(R.id.listview);
        swiplayout.setOnRefreshListener(onRefreshListener);
    }

    private void initdata() {
        p2pDevices = new ArrayList<>();
        adapter = new ItemAdapter(this, p2pDevices);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putParcelable("device", wifiP2pDevice);
                Intent intent = new Intent(MainActivity.this, ConnetActivity.class);
                intent.putExtra("info", bundle);
                startActivity(intent);
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            P2pServiceManager.getInstance().scanPeers(new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int reason) {
                    swiplayout.setRefreshing(false);
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        swiplayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swiplayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        },500);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onWifiP2pEnabled(boolean isenable) {
        if (!isenable) {
            Toast.makeText(MainActivity.this, "p2p不可用", Toast.LENGTH_SHORT).show();
        } else {
            swiplayout.setRefreshing(true);
            onRefreshListener.onRefresh();
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        swiplayout.setRefreshing(false);
        p2pDevices.clear();
        if(peers!=null){
            List<WifiP2pDevice> dess = new ArrayList<>();
            for (WifiP2pDevice device : peers.getDeviceList()) {
                dess.add(device);
            }
            p2pDevices.addAll(dess);
        }
        adapter.notifyDataSetChanged();

    }
}
