package com.ts.mobilepicklist.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.image.PhotoPreview;
import com.ts.mobilepicklist.image.TakePhotoTask;
import com.ts.mobilepicklist.network.RAP;
import com.ts.mobilepicklist.utils.CustomActivity;


public class ActTakeItemPhoto extends CustomActivity {

    private final static String DEBUG_TAG = "MobilePicklist";
    //private Camera camera;
    private PhotoPreview pPreview;
    private int cameraId = -1;
    private FrameLayout preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        setContentView(R.layout.activity_takeitemphoto);
//        try {
//            Thread.sleep(5000);
//        }catch (Exception ex){
//
//        }
        cameraId = -1;

        // do we have a camera?
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findBackCamera();
            if (cameraId < 0) {
                Log.d(DEBUG_TAG, "FindBackCamera return: " + cameraId);
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                RAP.getInstance().cameraDevice = Camera.open(cameraId);

            }
        }
        Log.d("MobilePickList", "Camera -> " + cameraId);
        pPreview =  new PhotoPreview(this);//, camera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(pPreview);
        RAP.getInstance().imageWasTaken = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(RAP.getInstance().cameraDevice == null){
                RAP.getInstance().cameraDevice = Camera.open(cameraId);
            }
            if(pPreview == null) {
                pPreview = new PhotoPreview(this.getApplicationContext());//, RAP.getInstance().cameraDevice);
                preview.addView(pPreview);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


//        if(camera!=null) {
//            Log.d(DEBUG_TAG, "IN ON POSTEXECUTE before camera release");
//            camera.stopPreview();
//            camera.setPreviewCallback(null);
//            pPreview.getHolder().removeCallback(pPreview);
//            camera.release();
//            camera = null;
//            Log.d(DEBUG_TAG, "IN ON POSTEXECUTE after camera release");
//
//        }
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
//                    .show();
//        } else {
//            cameraId = findBackCamera();
//            if (cameraId < 0) {
//                Log.d(DEBUG_TAG, "FindBackCamera return: " + cameraId);
//                Toast.makeText(this, "No front facing camera found.",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                camera = Camera.open(cameraId);
//            }
//        }
//        Log.d("MobilePickList", "Camera -> " + cameraId);
//        pPreview =  new PhotoPreview(this, camera);
//        preview = (FrameLayout) findViewById(R.id.camera_preview);
//        camera.startPreview();
//        preview.addView(pPreview);

    }

    @Override
    protected void onRestart(){
        super.onRestart();
//        try {
//            if(camera == null){
//                camera = Camera.open(cameraId);
//            }
//            if(preview == null) {
//                preview = (FrameLayout) findViewById(R.id.camera_preview);
//            }
//            //preview.removeAllViews();
//            pPreview = new PhotoPreview(this.getApplicationContext(), camera);
//            preview.addView(pPreview);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
        if(RAP.getInstance().cameraDevice!=null) {
            Log.d(DEBUG_TAG, "IN ON POSTEXECUTE before camera release");
            RAP.getInstance().cameraDevice.stopPreview();
            RAP.getInstance().cameraDevice.setPreviewCallback(null);
            pPreview.getHolder().removeCallback(pPreview);
            RAP.getInstance().cameraDevice.release();
            RAP.getInstance().cameraDevice = null;
            Log.d(DEBUG_TAG, "IN ON POSTEXECUTE after camera release");

        }
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findBackCamera();
            if (cameraId < 0) {
                Log.d(DEBUG_TAG, "FindBackCamera return: " + cameraId);
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                RAP.getInstance().cameraDevice = Camera.open(cameraId);
            }
        }
        Log.d("MobilePickList", "Camera -> " + cameraId);
        pPreview =  new PhotoPreview(this);//, camera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        RAP.getInstance().cameraDevice.startPreview();
        preview.addView(pPreview);


    }


    @Override
    protected void onPause() {
        super.onPause();
        //if(!RAP.getInstance().imageWasTaken) {
            try {
                // release the camera immediately on pause event
                //releaseCamera();
                //camera.stopPreview();
                if (pPreview != null) {
                    preview.removeView(pPreview);
                    preview = null;
                }
                if (RAP.getInstance().cameraDevice != null) {
                    Log.d(DEBUG_TAG, "IN ON POSTEXECUTE before camera release");
                    if(!RAP.getInstance().imageWasTaken) {
                        RAP.getInstance().cameraDevice.stopPreview();

                        RAP.getInstance().cameraDevice.setPreviewCallback(null);
                        RAP.getInstance().cameraDevice.lock();
                        RAP.getInstance().cameraDevice.release();
                        RAP.getInstance().cameraDevice = null;
                    }
                    //preview = null;
                    Log.d(DEBUG_TAG, "IN ON POSTEXECUTE after camera release");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        //}

        //konczy activity kiedy jest nieaktywna - również jeśli wygasimy egran / DO PRZEMYSLENIA???!!!
        //finish();
    }

    @Override
    protected  void onStart(){
        super.onStart();
        try {
            if(preview == null) {
                preview = (FrameLayout) findViewById(R.id.camera_preview);
            }
            //preview.removeAllViews();
            pPreview = new PhotoPreview(this.getApplicationContext());//, camera);
            preview.addView(pPreview);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void onClick(View view) {
        TakePhotoTask takePhotoTask = new TakePhotoTask(getApplicationContext(), getIntent(), pPreview);// RAP.getInstance().cameraDevice, pPreview);
        takePhotoTask.execute();

    }

    public void exitApp(View view){
        onPause();
        finish();
        System.exit(0);
    }


    private int findBackCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        Log.d(DEBUG_TAG, "NUMBER of CAMERA: " + numberOfCameras);
        //Toast.makeText(this, "Numer of camera: " + numberOfCameras, Toast.LENGTH_LONG).show();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            Log.d(DEBUG_TAG, "Facing id " + info.facing);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                return cameraId;
            }
        }
//        for (int i = 0; i < numberOfCameras; i++) {
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, info);
//            Log.d(DEBUG_TAG, "Facing id " + info.facing);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                Log.d(DEBUG_TAG, "Camera found");
//                cameraId = i;
//                return cameraId;
//            }
//        }
        return cameraId;
    }
}
