package android.jcgf.com.wifip2pdemo.activity.socket;

/**
 * Created by wenbaohe on 2018/9/25.
 */

public interface OnSocketListen {
    public void onsocketConnet(String ip);

    public void onsocketdisConnet();

    public void onsocketRecever(String msg);
}