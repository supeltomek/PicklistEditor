package com.ts.mobilepicklist.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ts250231 on 2015-04-24.
 */
public class PhotoPathHelper {

    public static PhotoPathHelper instance;
    public String photoPath;

    public static PhotoPathHelper getInstance(){
        if(instance == null){
            instance = new PhotoPathHelper();
        }
        return instance;
    }

    public void SetImagePath(String imagePath){
        this.photoPath = imagePath;
    }

    public String GetImagePath(){
        if(photoPath == null){

        }
        return photoPath;
    }

    public void ResizeImage(String imagePath) {
        boolean preserveAlpha = true;
        float scaledWidth = 122;
        float scaledHeight = 77;
        int max = 122;
        int maxWidth = 122;
        int maxHeight = 77;

        Bitmap oryginalImg = BitmapFactory.decodeFile(imagePath);
        int orgWidth = oryginalImg.getWidth();
        int orgHeight = oryginalImg.getHeight();
        float newWidth = 0;
        float newHight = 0;

        if(orgHeight==orgWidth){
            scaledHeight = (float)max;
            scaledWidth = (float)max;
        }
        else if(orgWidth>orgHeight){
            scaledWidth = (float)maxWidth;
            scaledHeight = (float)orgHeight/orgWidth * maxWidth;
        }
        else{
            scaledHeight = (float)maxHeight;
            scaledWidth = (float)orgWidth/orgHeight * maxHeight;
        }

        newWidth = (scaledWidth) / orgWidth;
        newHight = (scaledHeight) / orgHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(newWidth, newHight);

        matrix.postRotate(90);
        Bitmap resizedBitmap = null;
        Bitmap rectangleBitmap = Bitmap.createBitmap(122,77,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rectangleBitmap);
        canvas.drawColor(Color.WHITE);


        try {
            resizedBitmap = Bitmap.createBitmap(oryginalImg, 0, 0, oryginalImg.getWidth(), oryginalImg.getHeight(), matrix, false);
            canvas.drawBitmap(resizedBitmap, (122 - resizedBitmap.getWidth())/2, (77-resizedBitmap.getHeight())/2, null);

        }
        catch (Exception ex){
            Log.e("MobilePickList", ex.getMessage());
        }
        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Log.e("PickListEditor", "Can't create directory to save image.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = GetImagePath();

        try {
            FileOutputStream pictureFile = new FileOutputStream(filename, false);
            rectangleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, pictureFile);
//            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, pictureFile);
        } catch (Exception ex) {
            Log.d("PickListEditor", ex.getMessage());
        }
    }

    private File getDir(){
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        return new File(sdDir, "MobilePiclistFile");
    }
}
