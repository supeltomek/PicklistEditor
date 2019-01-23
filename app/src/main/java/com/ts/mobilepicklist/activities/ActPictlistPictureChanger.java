package com.ts.mobilepicklist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.image.PhotoPathHelper;
import com.ts.mobilepicklist.utils.CustomActivity;


public class ActPictlistPictureChanger extends CustomActivity {

    private final static String DEBUG_TAG = "MobilePicklist";
    private String picturePath = "";

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_changer);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        picturePath = PhotoPathHelper.getInstance().GetImagePath();
        Log.d(DEBUG_TAG, "PictureChanger -> Picture Path: " + picturePath);
        ImageView imgView = (ImageView) findViewById(R.id.imgView);
        if(picturePath != null) {
            Bitmap pictureBitmap = BitmapFactory.decodeFile(picturePath);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            pictureBitmap = Bitmap.createBitmap(pictureBitmap, 0, 0, pictureBitmap.getWidth(), pictureBitmap.getHeight(), matrix, true);
            imgView.setImageBitmap(pictureBitmap);

        }
        PhotoPathHelper.getInstance().ResizeImage(picturePath);
    }


    public void takeNewPhoto(View view){
        Intent intent = new Intent(view.getContext(), ActTakeItemPhoto.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }

    public void savePhoto(View view){
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_insertupctype, null);
        popupWindow = new PopupWindow(popupView, android.app.ActionBar.LayoutParams.FILL_PARENT, android.app.ActionBar.LayoutParams.FILL_PARENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);

    }

    public void popupScanUpc(View view){
        Toast.makeText(view.getContext(), "ScanUPC was clicked", Toast.LENGTH_LONG).show();
    }
    public void popupInsertUpc(View view) {
        //do nowego activity przekazac sciezke do pliku zdjecia
        popupWindow.dismiss();
        Intent intent = new Intent(view.getContext(), ActInsertUPC.class);

        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }

    public void popupInserUpcClose(View v){
        popupWindow.dismiss();
    }
    public void popupClose(View v){
        LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_insertupctype, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);

        popupWindow.dismiss();
    }


}
