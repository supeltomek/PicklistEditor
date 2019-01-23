package com.ts.mobilepicklist.image;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.ts.mobilepicklist.network.RAP;

import java.io.IOException;
import java.util.List;

/**
 * Created by ts250231 on 2015-04-23.
 */
public class PhotoPreview extends SurfaceView implements SurfaceHolder.Callback {

    private final static String DEBUG_TAG = "MobilePicklist";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public PhotoPreview(Context context){//, Camera camera) {
        super(context);
        mCamera = RAP.getInstance().cameraDevice;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
//        try {
            if(mCamera != null)
            {
                Camera.Parameters parameters = mCamera.getParameters();
                mCamera.setParameters(parameters);
//                mCamera.setPreviewDisplay(mHolder);
//                mCamera.startPreview();
            }
//        } catch (IOException e) {
//            Log.d(DEBUG_TAG, "Error setting camera preview in surfaceCreated: " + e.getMessage());
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        setCameraDefaults();
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);

            mCamera.startPreview();

        } catch (Exception e){
            Log.d(DEBUG_TAG, "Error starting camera preview in surfaceChanged: " + e.getMessage());
        }

    }
    private void setCameraDefaults()
    {
        Camera.Parameters params = null;
        try {
             params = mCamera.getParameters();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        // Supported picture formats (all devices should support JPEG).
        List<Integer> formats = params.getSupportedPictureFormats();

        if (formats.contains(ImageFormat.JPEG))
        {
            params.setPictureFormat(ImageFormat.JPEG);
            params.setJpegQuality(100);
        }
        else
            params.setPictureFormat(PixelFormat.RGB_565);

        // Now the supported picture sizes.
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size size = sizes.get(sizes.size()-1);
        params.setPictureSize(size.width, size.height);
        params.setRotation(90);

        // Set the brightness to auto.
        params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);

        // Set the flash mode to auto.
        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

        // Set the scene mode to auto.
        params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);

        // Lastly set the focus to auto.
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

//        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//
//        if(display.getRotation() == Surface.ROTATION_0)
//        {
//            params.setPreviewSize(size.height, size.width);
//            mCamera.setDisplayOrientation(90);
//        }
//
//        if(display.getRotation() == Surface.ROTATION_90)
//        {
//            params.setPreviewSize(size.height, size.width);
//
//        }
//
//        if(display.getRotation() == Surface.ROTATION_180)
//        {
//            params.setPreviewSize(size.height, size.width);
//
//        }
//
//        if(display.getRotation() == Surface.ROTATION_270)
//        {
//            params.setPreviewSize(size.height, size.width);
//
//            mCamera.setDisplayOrientation(180);
//        }
        mCamera.setDisplayOrientation(90);

        mCamera.setParameters(params);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        // przy przechodzeniu do innego activity rzuca wyjątek method call after release
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
