package android.jcgf.com.wifip2pdemo.activity.udp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wenbaohe on 2018/9/21.
 */

public class UDPThread extends Thread {

    /**
     * 接收部分
     */
    private DatagramSocket recesocket;
    private int RECE_PORT = 8800;
    private DatagramPacket recPacket;
    byte[] buffer = new byte[1024];

    private ArrayList<String> ips;

    public void setRECE_PORT(int RECE_PORT) {
        this.RECE_PORT = RECE_PORT;
    }

    public UDPThread() {


    }

    /**
     * 作为群组的需要先开启接收
     */
    public void startReceUdp() {
        this.start();
    }

    @Override
    public void run() {
        try {
            ips = new ArrayList<>();
            recesocket = new DatagramSocket(RECE_PORT);
            recPacket = new DatagramPacket(buffer, buffer.length);
            while (true) {
                Log.e("等待接收", "等待接收");
                recesocket.receive(recPacket);
                String s = new String(recPacket.getData(), recPacket.getOffset(), recPacket.getLength(), "UTF-8");
                String ipstr = recPacket.getAddress().getHostAddress();
                if (!ips.contains(ipstr)) {
                    ips.add(ipstr);
                    startSend(ipstr);
                }
                String hos = recPacket.getAddress().getCanonicalHostName() + "(" + recPacket.getAddress().getHostAddress() + ")";
                Log.e("接收到--->", hos);
                if (onUdpReceverListen != null) {
                    onUdpReceverListen.onRecever(hos + ":\n" + s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnUdpReceverListen onUdpReceverListen;

    public void setOnUdpReceverListen(OnUdpReceverListen onUdpReceverListen) {
        this.onUdpReceverListen = onUdpReceverListen;
    }

    public interface OnUdpReceverListen {
        public void onRecever(String str);
    }


    /**
     * 发送部分
     */
    private DatagramSocket sendsocket;
    private int SEND_PORT = 9000;
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void setSEND_PORT(int SEND_PORT) {
        this.SEND_PORT = SEND_PORT;
    }

    /**
     * 作为成员的先开启发送，因为必须要让群组知道成员的ip信息，成员才能接收到群组回传的信息
     */
    public void startSend(final String msg) {
        if (ips != null) {
            for (String ip : ips) {
                send(msg, ip);
            }
        }else {
             send(msg, "255.255.255.255");
        }
    }
    public void send(final String msg, final String ip) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    sendsocket = new DatagramSocket(SEND_PORT);
                    byte[] data = msg.getBytes("UTF-8");
                    DatagramPacket pack = new DatagramPacket(data, data.length, InetAddress.getByName(ip), SEND_PORT);
                    sendsocket.send(pack);
                    Log.e("发送--->", msg + ip);
                    if (sendsocket != null) {
                        sendsocket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void close() {
        if (recesocket != null) {
            recesocket.close();
        }
        if (sendsocket != null) {
            sendsocket.close();
        }
        singleThreadExecutor.shutdown();
    }

}
