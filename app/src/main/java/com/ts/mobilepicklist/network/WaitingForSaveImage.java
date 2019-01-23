package com.ts.mobilepicklist.network;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.utils.LayoutWorker;

/**
 * Created by ts250231 on 2015-06-15.
 */
public class WaitingForSaveImage extends AsyncTask<String, String, RapTcpClient> {

    private Context context;
    private View view;
    private LinearLayout llImageSaveLoad;
    private LinearLayout llImageConfirmation;
    private LinearLayout llImageSaved;
    private int[] LayoutIDS;

    public WaitingForSaveImage(Context context, View view){
        this.context = context;
        this.view = view;

        llImageSaveLoad = (LinearLayout)view.findViewById(R.id.llSaveImageLoad);
        llImageConfirmation = (LinearLayout)view.findViewById(R.id.llConfirmUPC);
        llImageSaved = (LinearLayout)view.findViewById(R.id.llUPCsaved);
        LayoutIDS = new int[]{R.id.llSaveImageLoad, R.id.llConfirmUPC, R.id.llUPCsaved};
    }

    @Override
    protected void onPreExecute(){
        LayoutWorker.INSTANCE.showLayoutById(view, LayoutIDS, R.id.llSaveImageLoad);
    }

    @Override
    protected RapTcpClient doInBackground(String... strings) {

        RAP.getInstance().mRapTcpClient = new RapTcpClient();
        int tmpTotalData = 0;
        for(int i =0; i<RAP.getInstance().imageChunkList.size(); i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RAP.getInstance().imageChunk = RAP.getInstance().imageChunkList.get(i);
            if(i== RAP.getInstance().imageChunkList.size() - 1) {
                RAP.getInstance().mRapTcpClient.sendMessageToRap("UPDATEEND");
            }
            else{
                RAP.getInstance().mRapTcpClient.sendMessageToRap("UPDATE");
            }
            tmpTotalData += RAP.getInstance().imageChunk.length;
        }
        RAP.getInstance().totalDataSend = Integer.toString(tmpTotalData);

        return null;
    }

    @Override
    protected void onPostExecute(RapTcpClient s){
        while(true){
            if(RAP.getInstance().saveItem()){
                break;
            }
        }
        LayoutWorker.INSTANCE.showLayoutById(view, LayoutIDS, R.id.llUPCsaved);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);


    }
}
