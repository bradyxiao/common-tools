package com.tencent.qcloud.audioapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by bradyxiao on 2018/1/9.
 */

public class MediaPlayerUtils {

    private MediaPlayer mediaPlayer;

    private PLAYER_STATE playerState; // 控制 mediaPlayer API的调用

    public MediaPlayerUtils(){
        mediaPlayer = new MediaPlayer();
        playerState = PLAYER_STATE.INIT;
    }

    public static MediaPlayer createMediaPlayer(Context context, Uri uri){
        return MediaPlayer.create(context, uri);
    }

    public static MediaPlayer createMediaPlayer(Context context, int resourceId){
        return MediaPlayer.create(context, resourceId);
    }

    /**
     *  reset
     *  setDataSource
     *  prepare
     *  start
     *  pause
     *  seekTo
     *  stop
     */
    public void setDataSource(String path) throws IOException {
        mediaPlayer.setDataSource(path);
    }

    public void setDataSource(FileDescriptor fileDescriptor, long offset, long length) throws IOException {
        mediaPlayer.setDataSource(fileDescriptor, offset, length);
    }

    public void setDataSource(FileDescriptor fileDescriptor) throws IOException {
        mediaPlayer.setDataSource(fileDescriptor);
    }

    public void setDataSource(Context context, Uri uri) throws IOException {
        mediaPlayer.setDataSource(context, uri);
    }

    public void setAudioStreamType(){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setDisplay(SurfaceHolder surfaceHolder){
        mediaPlayer.setDisplay(surfaceHolder);
    }

    public void reset(){
        mediaPlayer.reset();
    }

    public void prepare() throws IOException {
        playerState = PLAYER_STATE.PREPARE;
        mediaPlayer.prepare();
    }

    public void start(){
        if(playerState == PLAYER_STATE.PLAYING)return;
        if(playerState == PLAYER_STATE.PREPARE || playerState == PLAYER_STATE.PAUSE){
            mediaPlayer.start();
            playerState = PLAYER_STATE.PLAYING;
            return;
        }
        if(playerState == PLAYER_STATE.STOP){
            try {
                prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            start();
        }
    }

    public void seekTo(int mesc){
        mediaPlayer.seekTo(mesc);
    }

    public void pause(){
        if(playerState == PLAYER_STATE.PLAYING){
            mediaPlayer.pause();
            playerState = PLAYER_STATE.PAUSE;
        }
    }

    public void stop(){
        if(playerState == PLAYER_STATE.PLAYING){
            mediaPlayer.stop();
            playerState = PLAYER_STATE.STOP;
        }
    }

    public void looper(boolean isLooper){
        mediaPlayer.setLooping(isLooper);
    }

    public void setPreparedListner(MediaPlayer.OnPreparedListener onPreparedListener){
        mediaPlayer.setOnPreparedListener(onPreparedListener);
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener){
        mediaPlayer.setOnErrorListener(onErrorListener);
    }

    public void setOnCompletedistener(MediaPlayer.OnCompletionListener onCompletedistener){
        mediaPlayer.setOnCompletionListener(onCompletedistener);
    }

    public void setOnSeekCompletedListener(MediaPlayer.OnSeekCompleteListener onSeekCompletedListener){
        mediaPlayer.setOnSeekCompleteListener(onSeekCompletedListener);
    }

    public void setVideoWHChanged(MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener){
        mediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    }



    public static enum PLAYER_STATE{
        INIT,
        PREPARE,
        PLAYING,
        PAUSE,
        STOP
    }

}
