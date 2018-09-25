package android.jcgf.com.wifip2pdemo.activity;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.jcgf.com.wifip2pdemo.p2p.common.P2pServiceManager;
import android.jcgf.com.wifip2pdemo.p2p.common.WifiP2pService;
import android.os.IBinder;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public class MyAPPlocation extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        bindService(new Intent(this, WifiP2pService.class), p2pconnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection p2pconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            P2pServiceManager.getInstance().setService(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
