package com.ts.mobilepicklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.utils.CustomActivity;

public class ActActionType extends CustomActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_action_type);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
    }

    public void connectRap(View view){
        Intent intent = new Intent(view.getContext(), ActTakeItemPhoto.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }

    public void openSettings(View view){
        Intent intent = new Intent(view.getContext(), ActNetworkSettings.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//
//        PopupCloseApp popupCloseApp = new PopupCloseApp(this.findViewById(R.id.mainView).getRootView(), this.getApplicationContext());
//        popupCloseApp.DisplayPopup();
//
//    }
}
