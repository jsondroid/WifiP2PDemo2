package android.jcgf.com.wifip2pdemo.activity.socket;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wenbaohe on 2018/9/25.
 */

public class CSocket implements Runnable {

    private Socket socket;
    private boolean isacc = true;
    private ExecutorService executorService;
    private int port = 9292;
    private String ip;

    public CSocket() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public void startConnet(String ip) {
        this.ip = ip;
        executorService.execute(this);
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 5000);
            if (onSocketListen != null) {
                onSocketListen.onsocketConnet(ip);
            }
            // 定义输入输出流
            InputStream is = socket.getInputStream();
            recveMsg(is);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void recveMsg(final InputStream is) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        String text = new String(buffer, 0, len);
                        if (onSocketListen != null) {
                            onSocketListen.onsocketRecever(text);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMsg(final String msg) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                try {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 填充信息
                if (out != null) {
                    out.println(new String(msg));
                }
            }
        });
    }


    public void close() {
        isacc = false;
        if (socket != null) {
            try {
                socket.close();
                if (onSocketListen != null) {
                    onSocketListen.onsocketdisConnet();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (executorService != null) {
            executorService.shutdown();
        }

    }


    private OnSocketListen onSocketListen;

    public void setOnSocketListen(OnSocketListen onSocketListen) {
        this.onSocketListen = onSocketListen;
    }
}
