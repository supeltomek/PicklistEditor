package com.ncr.mobilepicklist.image;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ts250231 on 2015-04-23.
 */
public class PhotoHandler implements PictureCallback {

    private final static String DEBUG_TAG = "MobilePicklist";

    private final Context context;
    private String imagePath = null;
    private File pictureFile = null;

    public boolean pictureDone = false;

    public PhotoHandler(Context context){
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

        File pictureFileDir = getDir();

        if(!pictureFileDir.exists() && !pictureFileDir.mkdirs()){
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(bytes);
            fos.close();

            imagePath = pictureFile.getAbsolutePath();

            PhotoPathHelper.getInstance().SetImagePath(imagePath);
            Log.d("MobilePicklist", "Image path set to: " + imagePath);
            pictureDone = true;
        } catch (Exception error) {
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
        //camera.startPreview();
        //Log.d(DEBUG_TAG, "After start preview");


    }


    private File getDir(){
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        return new File(sdDir, "MobilePiclistFile");
    }
}
