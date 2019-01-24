package com.ts.mobilepicklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.utils.CustomActivity;

import java.util.ArrayList;

//import com.ncr.mobilepicklist.network.PopupNetworkList;

public class ActNetworkSettings extends CustomActivity {


//    private ExpandableListAdapter adapter;
//    private ExpandableListView expandableListView;
//    List<String> listDataHeader;
//    HashMap<String, List<String>> listDataChild;

    ArrayList<String> networkNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_network);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


    }


    public void showNetworkList(View view){
        //PopupNetworkList popupNetworkList = new PopupNetworkList(view, getApplicationContext(), getBaseContext());
        //popupNetworkList.DisplayPopup();
    }

    public void connectNetwork(View view){
        Intent intent = new Intent(view.getContext(), ActActionType.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }


    private void prepareListData() {
        networkNames.add("1");
        networkNames.add("2");
        networkNames.add("3");
    }
}
