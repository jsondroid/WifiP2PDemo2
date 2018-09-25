package android.jcgf.com.wifip2pdemo.activity;

import android.content.Intent;
import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.p2p.common.P2pServiceManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void creatGroup(View view) {
        P2pServiceManager.getInstance().createGroup(new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(HomeActivity.this, GroupActivity.class));
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(HomeActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                P2pServiceManager.getInstance().removeGroup(null);
            }
        });
    }

    public void removeGroup(View view) {
        P2pServiceManager.getInstance().removeGroup(new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(HomeActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(HomeActivity.this, "移除失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addGroup(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        P2pServiceManager.getInstance().removeGroup(null);
    }
}
