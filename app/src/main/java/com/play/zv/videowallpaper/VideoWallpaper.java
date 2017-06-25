package com.play.zv.videowallpaper;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Zv on 2017/06/25.
 */

public class VideoWallpaper extends WallpaperService {
    public  String vedioPath;
    private MediaPlayer mMediaPlayer;

    @Override
    public Engine onCreateEngine() {
        System.out.println("onCreateEngine"+vedioPath);
        return new VideoEngine();
    }

    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.play.zv";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 110;
    public static final int ACTION_VOICE_NORMAL = 111;

    public static void voiceSilence(Context context) {
        Intent intent = new Intent(VideoWallpaper.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoWallpaper.KEY_ACTION, VideoWallpaper.ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    public static void voiceNormal(Context context) {
        Intent intent = new Intent(VideoWallpaper.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoWallpaper.KEY_ACTION, VideoWallpaper.ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    public void setToWallPaper(Context context,String path) {
        System.out.println("先到这里初始化？");
        vedioPath = path;
        System.out.println("先到这里初始化！"+vedioPath);
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, VideoWallpaper.class));
        context.startActivity(intent);
    }


    class VideoEngine extends Engine {





        private BroadcastReceiver mVideoParamsControlReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);


            IntentFilter intentFilter = new IntentFilter(VIDEO_PARAMS_CONTROL_ACTION);
            registerReceiver(mVideoParamsControlReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //L.d("onReceive");
                    int action = intent.getIntExtra(KEY_ACTION, -1);

                    switch (action) {
                        case ACTION_VOICE_NORMAL:
                            mMediaPlayer.setVolume(1.0f, 1.0f);
                            break;
                        case ACTION_VOICE_SILENCE:
                            mMediaPlayer.setVolume(0, 0);
                            break;

                    }
                }
            }, intentFilter);


        }

        @Override
        public void onDestroy() {
            //L.d("VideoEngine#onDestroy");
            unregisterReceiver(mVideoParamsControlReceiver);
            super.onDestroy();

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            //L.d("VideoEngine#onVisibilityChanged visible = " + visible);
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            System.out.println("onSurfaceCreated");
            super.onSurfaceCreated(holder);
            mMediaPlayer = new MediaPlayer();

            try {
//                AssetManager assetMg = getApplicationContext().getAssets();
//                AssetFileDescriptor fileDescriptor = assetMg.openFd("test1.mp4");
                //if (!vedioPath.isEmpty()) {
                System.out.println("到这里了" + vedioPath);
                mMediaPlayer.setDataSource("/storage/emulated/0/DCIM/Camera/VID_20170521_150414.mp4");
                //System.out.println(vedioPath);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.prepare();
                //mMediaPlayer.prepareAsync();
                mMediaPlayer.start();
                mMediaPlayer.setSurface(holder.getSurface());
                //}

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //L.d("VideoEngine#onSurfaceChanged ");
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            //L.d("VideoEngine#onSurfaceDestroyed ");
            super.onSurfaceDestroyed(holder);
            mMediaPlayer.release();
            mMediaPlayer = null;

        }
    }
}

