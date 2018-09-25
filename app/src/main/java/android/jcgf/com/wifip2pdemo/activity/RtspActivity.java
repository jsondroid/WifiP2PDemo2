package android.jcgf.com.wifip2pdemo.activity;

import android.jcgf.com.wifip2pdemo.R;
import android.jcgf.com.wifip2pdemo.p2p.common.MainHandler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.videolan.vlc.VlcVideoView;
import org.videolan.vlc.listener.MediaListenerEvent;

public class RtspActivity extends AppCompatActivity implements MediaListenerEvent{
    private VlcVideoView vlcVideoView;
    private TextView textView;
    private EditText edt_adress;

    private String ip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtsp);
        vlcVideoView = findViewById(R.id.player);
        textView = findViewById(R.id.textView);
        edt_adress = findViewById(R.id.edt_adress);

        ip = getIntent().getStringExtra("ip");

        vlcVideoView.setMediaListenerEvent(this);
        vlcVideoView.setKeepScreenOn(true);
        String ad=formatRtsp(ip);
        vlcVideoView.setPath(ad);
        edt_adress.setText(ad);
        vlcVideoView.startPlay();
    }

    public void startRtsp(View view){
        vlcVideoView.pause();
        String ads=edt_adress.getText().toString();
        vlcVideoView.setPath(ads);
        vlcVideoView.startPlay();
    }

    public static String formatRtsp(String ip) {
        return "rtsp://" + ip + "/live.sdp";
    }

    @Override
    protected void onResume() {
        super.onResume();
        vlcVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        vlcVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vlcVideoView.onDestory();
    }

    @Override
    public void eventBuffing(int event,final float buffing) {
        MainHandler.getInstance().runInMainThread(new Runnable() {
            @Override
            public void run() {
                if(buffing>=100){
                    textView.setText(buffing+"%");
                    textView.setVisibility(View.GONE);
                }else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(buffing+"%");
                }
            }
        });


    }

    @Override
    public void eventStop(boolean isPlayError) {

    }

    @Override
    public void eventError(int event, boolean show) {

    }

    @Override
    public void eventPlay(boolean isPlaying) {

    }

    @Override
    public void eventPlayInit(boolean openClose) {

    }
}
