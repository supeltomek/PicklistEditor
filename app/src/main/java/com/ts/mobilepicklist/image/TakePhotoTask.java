package com.ts.mobilepicklist.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import com.ts.mobilepicklist.activities.ActPictlistPictureChanger;
import com.ts.mobilepicklist.network.RAP;

/**
 * Created by ts250231 on 2015-04-24.
 */
public class TakePhotoTask extends AsyncTask<Void, Void, Void> {

    private final static String DEBUG_TAG = "MobilePicklist";

    //private Camera camera;
    private Context context;
    private Intent intent;
    private PhotoPreview preview;
    private PhotoHandler photoHandler;

    public TakePhotoTask(Context context, Intent intent, PhotoPreview preview){// Camera camera, PhotoPreview preview){
        //this.camera = camera;
        this.context = context;
        this.preview = preview;
        this.intent = intent;
        this.photoHandler = new PhotoHandler(context);
    }

    @Override
    protected void onPostExecute(Void result){
        Log.d("MobilePicklist", "Image path after execute: " + PhotoPathHelper.getInstance().GetImagePath());
        //camera.stopPreview();
        if(RAP.getInstance().cameraDevice!=null) {
            Log.d(DEBUG_TAG, "IN ON POSTEXECUTE before camera release");
            RAP.getInstance().cameraDevice.stopPreview();
            RAP.getInstance().cameraDevice.setPreviewCallback(null);
            preview.getHolder().removeCallback(preview);
            RAP.getInstance().cameraDevice.release();
            RAP.getInstance().cameraDevice = null;
            Log.d(DEBUG_TAG, "IN ON POSTEXECUTE after camera release");

        }
        if(photoHandler.pictureDone) {
            Log.d(DEBUG_TAG, "TakePhotoTask -> pictureDone: " + photoHandler.pictureDone);
            RAP.getInstance().imageWasTaken = true;
            Intent intent = new Intent(context.getApplicationContext(), ActPictlistPictureChanger.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
            context.getApplicationContext().startActivity(intent);

            //((Activity)context.getApplicationContext()).finish();
        }
        else {
            Log.d(DEBUG_TAG, "TakePhotoTask -> pictureDone: " + photoHandler.pictureDone);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //PhotoHandler photoHandler = new PhotoHandler(context);
        RAP.getInstance().cameraDevice.takePicture(null, null, photoHandler);

        try{
            Thread.sleep(3000);
        }catch (Exception ex){
            Log.d("MobilePiclist", ex.getMessage());
        }
        return null;
    }

}
