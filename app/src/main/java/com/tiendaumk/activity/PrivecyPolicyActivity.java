package com.tiendaumk.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;
import com.tiendaumk.R;
import com.tiendaumk.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;
import static com.tiendaumk.utils.SessionManager.privacy;

public class PrivecyPolicyActivity extends BaseActivity {




    @BindView(R.id.img_icon)
    ImageView imgIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privecypolicy);
        ButterKnife.bind(this);

        imgIcon.setVisibility(View.VISIBLE);




        Glide.with(getBaseContext()).load("http://192.168.1.139:8448/tnd/banner/609562443de4bBanner%20Ayuda%20APP%20UNIMARK.png" ).thumbnail(Glide.with(getBaseContext()).load(R.drawable.lodingimage)).into(imgIcon);

    }






    public void btToggleClick(View view) {
        if (view instanceof ImageView) {
            ImageView img = (ImageView) view;
            if (img.getId() == R.id.dropdown1) {
                if (view.isSelected()) {
                    imgIcon.setVisibility(View.GONE);
                    img.setImageResource(R.drawable.ic_arrow_drop);

                } else {
                    imgIcon.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.ic_arrow_drop_up);
                }
                img.setSelected(!img.isSelected());
            } else if (img.getId() == R.id.dropdown2) {
                startActivity(new Intent(PrivecyPolicyActivity.this, ActivityHelp.class));
                img.setSelected(!img.isSelected());
            }
        }
    }
}
