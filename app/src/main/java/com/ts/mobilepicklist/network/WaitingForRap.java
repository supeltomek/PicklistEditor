package com.ts.mobilepicklist.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.activities.ActTakeItemPhoto;
import com.ts.mobilepicklist.utils.LayoutWorker;

import java.util.Objects;

/**
 * Created by ts250231 on 2015-04-29.
 */
public class WaitingForRap extends AsyncTask<String, Void, String>{

    private Context context;
    private View view;
    private LinearLayout llInsertRapIP;
    private LinearLayout llConnectingToRapLoad;
    private LinearLayout llConnectionToRapResult;
    private LinearLayout llConnectionToRapError;
    private int[] LayoutIDS;


    public WaitingForRap(Context context, View view){
        this.context = context;
        this.view = view;

        llInsertRapIP = (LinearLayout)view.findViewById(R.id.llInsertRapIP);
        llConnectingToRapLoad = (LinearLayout)view.findViewById(R.id.llConnectToRapLoad);
        llConnectionToRapResult = (LinearLayout)view.findViewById(R.id.llConnectToRapResult);
        llConnectionToRapError = (LinearLayout)view.findViewById(R.id.llConnectToRapError);
        LayoutIDS = new int[]{R.id.llInsertRapIP, R.id.llConnectToRapLoad, R.id.llConnectToRapResult, R.id.llConnectToRapError};
    }


    @Override
    protected void onPreExecute(){
        //showLayoutById(R.id.llConnectToRapLoad);
        LayoutWorker.INSTANCE.showLayoutById(view, LayoutIDS, R.id.llConnectToRapLoad);
    }


    @Override
    protected String doInBackground(String[] strings) {
        Log.d("MobilePicklist", "YOU ARE IN DOINBACGROUND");
        boolean isConnected = new WifiConnected(context).isConnected();
        Log.d("MobilePicklist", "WIFIConnected return: " + isConnected);

        RAP.getInstance().mRapTcpClient.sendMessageToRap("HELLO_");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        }


        return null;
    }

    @Override
    protected void onPostExecute(String s){

        while(true) {
            if(RAP.getInstance().isConnectedToRap || RAP.getInstance().comunicationError){
                break;
            }
        }
        if(!RAP.getInstance().categoryList.isEmpty()){
            Intent intent = new Intent(view.getContext(), ActTakeItemPhoto.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);

        }
        else{
//            showLayoutById(R.id.llConnectToRapError);
            LayoutWorker.INSTANCE.showLayoutById(view, LayoutIDS, R.id.llConnectToRapError);
        }

    }

    private void showLayoutById(int layoutId){
        LinearLayout linearLayout;
        for(LAYOUTID layout : LAYOUTID.values()){
            linearLayout = (LinearLayout)view.findViewById(layout.id);
            Log.d("MobilePicklist", "Layout id: " + layout.id);
            if(layout.id==layoutId){
                linearLayout.setVisibility(View.VISIBLE);
                Log.d("MobilePicklist", "Show layout id: " + layout.id);
            }
            if(layout.id!=layoutId) {
                linearLayout.setVisibility(View.GONE);
                Log.d("MobilePicklist", "Disable layout id: " + layout.id);
            }
        }
    }
    private enum LAYOUTID{
        GETIP (R.id.llInsertRapIP),
        LOAD (R.id.llConnectToRapLoad),
        RESULT (R.id.llConnectToRapResult),
        ERROR (R.id.llConnectToRapError);

        int id;
        private LAYOUTID(int id){
            this.id = id;
        }
    }
}
