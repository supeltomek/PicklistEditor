package com.ts.mobilepicklist.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * Created by ts250231 on 2015-04-28.
 */
public class CustomActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    }

    @Override
    public void onBackPressed(){
        PopupCloseApp popupCloseApp = new PopupCloseApp(getWindow().getDecorView().getRootView(), this.getApplicationContext());
        popupCloseApp.DisplayPopup();
    }

//    @Override
//    public void onAttachedToWindow(){
//        super.onAttachedToWindow();
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_HOME){
            PopupCloseApp popupCloseApp = new PopupCloseApp(getWindow().getDecorView().getRootView(), this.getApplicationContext());
            popupCloseApp.DisplayPopup();
        }

        return super.onKeyDown(keyCode, event);
    }

}
