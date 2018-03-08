package com.tencent.qcloud.audioapp.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.tencent.qcloud.audioapp.Main2Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by bradyxiao on 2018/1/15.
 */

public class VideoCameraView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG = "VideoCameraView";

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;

    public VideoCameraView(Context context) {
        this(context, null);
    }

    public VideoCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoCameraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init(){
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // surface created , so it can preview.
        Log.d(TAG, "Surface created");
        if(holder.getSurface() == null){
            return;
        }
        openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Surface changed");
        setCameraDisplayOrientation((Main2Activity) getContext(), Camera.CameraInfo.CAMERA_FACING_FRONT, mCamera);
        startPreviewDisplay(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface destroyed");
        stopPreviewDisplay();
    }

    public void openCamera(int cameraId) {
        try{
            mCamera = Camera.open(cameraId);
            setCamera(mCamera);
        }catch(Exception e) {
            Log.e(TAG, "Error while Open camera", e);
        }
    }

    private void setCamera(Camera camera) {
        mCamera = camera;
        final Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);
    }

     public  void setCameraDisplayOrientation(Activity activity,
             int cameraId, android.hardware.Camera camera) {
         android.hardware.Camera.CameraInfo info =
                 new android.hardware.Camera.CameraInfo();
         android.hardware.Camera.getCameraInfo(cameraId, info);
         int rotation = activity.getWindowManager().getDefaultDisplay()
                           .getRotation();
         int degrees = 0;
         switch (rotation) {
             case Surface.ROTATION_0: degrees = 0; break;
              case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
              case Surface.ROTATION_270: degrees = 270; break;
          }

          int result;
         if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
              result = (info.orientation + degrees) % 360;
              result = (360 - result) % 360;  // compensate the mirror
          } else {  // back-facing
             result = (info.orientation - degrees + 360) % 360;
         }
          camera.setDisplayOrientation(result);
     }

    private void startPreviewDisplay(SurfaceHolder holder){
        checkCamera();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error while START preview for camera", e);
        }
    }

    private void checkCamera(){
        if(mCamera == null) {
            throw new IllegalStateException("Camera must be set when start/stop preview, call <setCamera(Camera)> to set");
        }
    }

    private void stopPreviewDisplay(){
        checkCamera();
        try {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        } catch (Exception e){
            Log.e(TAG, "Error while STOP preview for camera", e);
        }
    }


    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private void save(byte[] data){
        File file = new File(((Main2Activity) getContext()).getExternalCacheDir().getPath() + "/test.jpg");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveRevisly(byte[] data){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        bitmap = rotateBitmapByDegree(bitmap, 270);
        File file = new File(((Main2Activity) getContext()).getExternalCacheDir().getPath() + "/" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture(){
        if(mCamera != null){
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    saveRevisly(data);
                    Toast.makeText((Main2Activity) getContext(), "Over", Toast.LENGTH_SHORT).show();
                    mCamera.startPreview();
                }
            });
        }
    }
}
