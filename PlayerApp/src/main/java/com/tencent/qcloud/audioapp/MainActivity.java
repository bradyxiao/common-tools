package com.tencent.qcloud.audioapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayerUtils mediaPlayerUtils;

    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();


        testSurfaceAndMediaPlayer();

        grantPermission();
    }

    private void initUI(){
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mediaPlayerUtils.start();
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerUtils.pause();
            }
        });


        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerUtils.stop();
            }
        });

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerUtils.reset();
            }
        });


    }
//
//    public void testAudio() throws IOException {

//        mediaPlayerUtils = new MediaPlayerUtils();
//        mediaPlayerUtils.reset();
//
//        mediaPlayerUtils.setDataSource(this, Uri.parse("android.resource://" + getPackageName()
//                + "/" + R.raw.luzhouyue));
//
//        mediaPlayerUtils.prepare();
//    }

//    public void testVideoView(){
//        mediaController = new MediaController(this);
//        VideoView videoView = findViewById(R.id.videoView);
//        videoView.setVideoPath(Environment.getExternalStorageDirectory() + "/test.mp4");
//        videoView.setMediaController(mediaController);
//        mediaController.setMediaPlayer(videoView);
//        mediaController.requestFocus();
//    }

    public void testSurfaceAndMediaPlayer(){


        SurfaceView surfaceView = findViewById(R.id.surfaceView);

        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //surfaceView ==> is created
                testVideo(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("MediaPlayerUtils", "holder_width =" + width + "| holder_height =" + height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    public void testVideo(final SurfaceHolder holder){
        mediaPlayerUtils = new MediaPlayerUtils();
        mediaPlayerUtils.reset();
        mediaPlayerUtils.setAudioStreamType();
        try {
            mediaPlayerUtils.setDataSource(Environment.getExternalStorageDirectory() + "/test.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayerUtils.setDisplay(holder);
        try {
            mediaPlayerUtils.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayerUtils.setPreparedListner(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d("MediaPlayerUtils", "duration =" + mp.getDuration() +
                        "| width =" + mp.getVideoWidth() + "| height =" + mp.getVideoHeight());
            }
        });

        mediaPlayerUtils.setVideoWHChanged(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.d("MediaPlayerUtils", "change_width =" + width + "| change_height =" + height);
                holder.setFixedSize(width, height);
            }
        });
    }

    private void grantPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            int granted = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    + checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) + checkSelfPermission(Manifest.permission.CAMERA);

            if(granted != PackageManager.PERMISSION_GRANTED){
                //grant
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
            }
        }
    }
}
