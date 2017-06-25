package com.play.zv.videowallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button set_wall_pager;
    private Context mContext;
    private CheckBox check_voice;
    private EditText set_video_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        initView();
        initListener();
    }

    private void initView() {
        set_wall_pager = (Button) findViewById(R.id.set_wall_pager);
        check_voice = (CheckBox) findViewById(R.id.check_voive);
        set_video_path = (EditText) findViewById(R.id.set_video_path);
    }

    private void initListener() {
        set_wall_pager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(set_video_path.getText().toString());
                VideoWallpaper videoWallpaper = new VideoWallpaper();

                    videoWallpaper.setToWallPaper(mContext,set_video_path.getText().toString());


            }
        });
        check_voice.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            // 静音
                            VideoWallpaper.voiceSilence(getApplicationContext());
                        } else {
                            VideoWallpaper.voiceNormal(getApplicationContext());
                        }
                    }
                });
    }
}
