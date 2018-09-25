package android.jcgf.com.wifip2pdemo.activity;

import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.activity.udp.UDPThread;
import android.jcgf.com.wifip2pdemo.p2p.common.MainHandler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class UDPActivity extends AppCompatActivity implements UDPThread.OnUdpReceverListen {

    private TextView tv_msg;
    private EditText edt_msg;
    private ScrollView scrollView;

    private String ip = "";
    private int type;
    private UDPThread udpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        tv_msg = findViewById(R.id.msg);
        edt_msg = findViewById(R.id.edt_msg);
        scrollView = findViewById(R.id.scrollView);


        ip = getIntent().getStringExtra("ip");
        type = getIntent().getIntExtra("type", 0);

        udpService = new UDPThread();
        udpService.setOnUdpReceverListen(this);
        if (type == 3) {//客户端
            udpService.setRECE_PORT(8800);
            udpService.setSEND_PORT(9000);
            udpService.send("",ip);
            udpService.startReceUdp();

        } else if (type == 4) {//组
            udpService.setRECE_PORT(9000);
            udpService.setSEND_PORT(8800);
            udpService.startReceUdp();
            udpService.send("","255.255.255.255");//发送给所有成员接收
        }

    }


    public void sendUdp(View view) {
        if (!TextUtils.isEmpty(ip)) {
            String msgs = edt_msg.getText().toString();
            if (type == 3) {//客户端
                udpService.send(msgs,ip);
            } else if (type == 4) {//组
                 udpService.startSend(msgs);
            }
        }
    }

    @Override
    public void onRecever(final String msg) {
        MainHandler.getInstance().runInMainThread(new Runnable() {
            @Override
            public void run() {
                tv_msg.append("\n------------------------\n接收到的信息：\n" + msg + "\n");
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                },300);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(udpService!=null){
            udpService.close();
        }
    }
}
