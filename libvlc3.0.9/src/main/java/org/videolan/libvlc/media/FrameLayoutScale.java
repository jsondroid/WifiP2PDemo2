package org.videolan.libvlc.media;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * Created
 */

public class FrameLayoutScale extends FrameLayout {

    private float videoScale = 10f / 16f;
    private boolean isFullVideoState;

    public FrameLayoutScale(Context context) {
        super(context);
    }

    public FrameLayoutScale(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutScale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isFullVideoState = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        onMeasureVideo(widthMeasureSpec, heightMeasureSpec);
    }

    //测量视频的大小
    private void onMeasureVideo(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        measureHeight = isFullVideoState ? measureHeight : (int) (measureWidth * videoScale);
        setMeasuredDimension(measureWidth, measureHeight);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.AT_MOST);
        measureChildren(widthMeasureSpec, newHeightMeasureSpec);
    }

    public void setScaleVideo(float videoScale) {
        this.videoScale = videoScale;
        invalidate();
    }

}