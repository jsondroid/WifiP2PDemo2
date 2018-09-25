package android.jcgf.com.wifip2pdemo.activity;

import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.activity.socket.CSocket;
import android.jcgf.com.wifip2pdemo.activity.socket.OnSocketListen;
import android.jcgf.com.wifip2pdemo.activity.socket.SSocket;
import android.jcgf.com.wifip2pdemo.p2p.common.MainHandler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class SocketActivity extends AppCompatActivity implements OnSocketListen {
    private TextView tv_msg;
    private EditText edt_msg;
    private ScrollView scrollView;

    private String ip = "";
    private int type;

    private SSocket sSocket;
    private CSocket cSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        tv_msg = findViewById(R.id.msg);
        edt_msg = findViewById(R.id.edt_msg);
        scrollView = findViewById(R.id.scrollView);

        ip = getIntent().getStringExtra("ip");
        type = getIntent().getIntExtra("type", 0);

        if (type == 3) {//客户端
            cSocket = new CSocket();
            cSocket.setOnSocketListen(this);
            cSocket.startConnet(ip);
        } else if (type == 4) {//组
            sSocket = new SSocket();
            sSocket.setOnSocketListen(this);
            sSocket.startConnet();
        }

    }

    /**
     * 发送信息
     */
    public void sendSocket(View view) {
        String text = edt_msg.getText().toString();
        if (type == 3) {//客户端
            cSocket.sendMsg(text);
        } else if (type == 4) {//组
            sSocket.sendMsg(text);
        }
    }

    @Override
    public void onsocketConnet(final String ip) {
        MainHandler.getInstance().runInMainThread(new Runnable() {
            @Override
            public void run() {
                tv_msg.append("\n已连接设备：" + ip);
                scoto();
            }
        });

    }

    @Override
    public void onsocketdisConnet() {
        MainHandler.getInstance().runInMainThread(new Runnable() {
            @Override
            public void run() {
                tv_msg.append("已断开连接");
                scoto();
            }
        });
    }

    @Override
    public void onsocketRecever(final String msg) {
        MainHandler.getInstance().runInMainThread(new Runnable() {
            @Override
            public void run() {
                tv_msg.append("\n接收到的信息：\n" + msg);
                scoto();
            }
        });
    }

    private void scoto() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 300);
    }
}
