package android.jcgf.com.wifip2pdemo.p2p.common;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by wenbaohe on 2018/6/20.
 */

public class MainHandler {

    private final Handler handler;


    public MainHandler() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    private static class HandlerClass {
        private static final MainHandler instance = new MainHandler();
    }


    public static MainHandler getInstance() {
        return HandlerClass.instance;
    }

    public void runInMainThread(Runnable runnable) {
        handler.post(runnable);
    }
}
