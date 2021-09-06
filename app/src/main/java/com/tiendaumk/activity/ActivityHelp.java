package com.tiendaumk.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.tiendaumk.R;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.jarvanmo.exoplayerview.media.ExoMediaSource;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.media.SimpleQuality;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;

import java.util.ArrayList;
import java.util.List;

import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;

public class ActivityHelp extends AppCompatActivity {


    private ExoVideoView videoView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);



        videoView = findViewById(R.id.videoView);


        videoView.setBackListener((view, isPortrait) -> {
            if (isPortrait) {
                finish();
            }
            return false;
        });



        videoView.setOrientationListener(orientation -> {
            if (orientation == SENSOR_PORTRAIT) {
                changeToPortrait();
            } else if (orientation == SENSOR_LANDSCAPE) {
                changeToLandscape();
            }
        });




        SimpleMediaSource mediaSource = new SimpleMediaSource("http://192.168.1.139:8448/tnd/product/video_ayuda.mp4");


        videoView.play(mediaSource, false);

        videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

       /* modeFit.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT));
        modeWidth.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH));
        modeHeight.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT));
        modeNone.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL));
        modeZoom.setOnClickListener(v -> videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM));*/
    }

    private void changeToPortrait() {

        WindowManager.LayoutParams attr = getWindow().getAttributes();
//        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        window.setAttributes(attr);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    private void changeToLandscape() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.resume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= 23)) {
            videoView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            videoView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.releasePlayer();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return videoView.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}