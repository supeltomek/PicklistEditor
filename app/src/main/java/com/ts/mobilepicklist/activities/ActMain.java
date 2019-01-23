package com.ts.mobilepicklist.activities;

import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.network.RAP;
import com.ts.mobilepicklist.network.WaitingForRap;
import com.ts.mobilepicklist.utils.CustomActivity;

public class ActMain extends CustomActivity {

    private EditText txRapIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        txRapIP = (EditText) findViewById(R.id.txtRAPIP);

        if(RAP.getInstance().getRapIP() != null)
            if(RAP.getInstance().getRapIP() != "") {
                Log.d("MobilePickList", "RAP IP = " + RAP.getInstance().getRapIP());
                WaitingForRap waitingForRap = new WaitingForRap(this.getApplicationContext(), this.getWindow().getDecorView().getRootView());
                waitingForRap.execute();
            }

    }

    public void reconnect(View view){
        finish();
        startActivity(getIntent());
    }

    public void changeIP(View view){
        RAP.getInstance().setRapIP(getApplicationContext(), "");
        finish();
        startActivity(getIntent());

    }

    public void connectToRAP(View view){
        Log.d("MobilePickList", "Trying connect to RAP");
        Log.d("MobilePickList", "RAP IP = " + txRapIP.getText().toString());
        RAP.getInstance().setRapIP(getApplicationContext(), txRapIP.getText().toString());

        WaitingForRap waitingForRap = new WaitingForRap(this.getApplicationContext(), this.getWindow().getDecorView().getRootView());
        waitingForRap.execute();
    }


}
