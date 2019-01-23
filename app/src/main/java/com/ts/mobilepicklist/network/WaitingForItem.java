package com.ts.mobilepicklist.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.activities.ActSaveItemConfirm;
import com.ts.mobilepicklist.utils.LayoutWorker;

import java.io.IOException;

/**
 * Created by ts250231 on 2015-06-15.
 */
public class WaitingForItem extends AsyncTask<String, Void, String> {
    private Context context;
    private View view;

    private int[] LayoutIDS;
    private LinearLayout llInsertUPC;
    private LinearLayout llCheckingItemLoad;
    private LinearLayout llInsertUPCandCategory;

    public WaitingForItem(Context context, View view){
        this.context = context;
        this.view = view;

        llInsertUPC = (LinearLayout)view.findViewById(R.id.llInsertUPC);
        llCheckingItemLoad = (LinearLayout)view.findViewById(R.id.llCheckingItemLoad);
        llInsertUPCandCategory = (LinearLayout)view.findViewById(R.id.llInsertUPCandCategory);
        LayoutIDS = new int[]{R.id.llInsertUPC, R.id.llCheckingItemLoad, R.id.llInsertUPCandCategory};

    }


    @Override
    protected void onPreExecute(){
        LayoutWorker.INSTANCE.showLayoutById(view, LayoutIDS, R.id.llCheckingItemLoad);
    }

    @Override
    protected String doInBackground(String... strings) {

        //if(RAP.getInstance().isConnectedToRap){
            try {
//                RAP.getInstance().sendLookupMessage(RAP.getInstance().insertedUPC);

                RAP.getInstance().mRapTcpClient.sendMessageToRap("LOOKUP|" + RAP.getInstance().prepareStringUPCcode(RAP.getInstance().insertedUPC));
                //RAP.getInstance().mRapTcpClient.sendMessage("LOOKUP|" + RAP.getInstance().prepareStringUPCcode(itemUPC));
                Log.d("MobilePickList", "Send lookup message to RAP with item code: " + RAP.getInstance().insertedUPC);
                Thread.sleep(500);
            } catch (Exception e) {
                Log.e("MobilePickList", "Error while trying to send lookup message: " + e.getMessage());
            }
        //}

        return null;
    }

    @Override
    protected void onPostExecute(String s){
        while(true){
            if(RAP.getInstance().isItemPrepared){
                break;
            }
        }
        if(RAP.getInstance().itemName.equals("NULL")){
            LayoutWorker.INSTANCE.showLayoutById(view, LayoutIDS, R.id.llInsertUPCandCategory);
        }
        else{
            Log.d("MobilePickList", "UPC exists, go to save confirmation window.");
            Intent intent = new Intent(view.getContext(), ActSaveItemConfirm.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);//, Activity.RESULT_OK);
            //finish();
        }

    }
}
