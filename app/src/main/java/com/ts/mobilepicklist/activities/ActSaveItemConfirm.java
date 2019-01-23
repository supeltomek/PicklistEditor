package com.ts.mobilepicklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.image.PhotoPathHelper;
import com.ts.mobilepicklist.network.WaitingForSaveImage;
import com.ts.mobilepicklist.network.RAP;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

public class ActSaveItemConfirm extends ActionBarActivity {

    TextView itemDescription = null;
    LinearLayout llItemUPCsaved = null;
    LinearLayout llConfirmUPC = null;
    TextView txtItemConfirmMessage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_save_item_confirm);

        itemDescription = (TextView) findViewById(R.id.txtItemDescription);
        llItemUPCsaved = (LinearLayout) findViewById(R.id.llUPCsaved);
        llConfirmUPC = (LinearLayout) findViewById(R.id.llConfirmUPC);
        txtItemConfirmMessage = (TextView)findViewById(R.id.txtItemConfirmMessage);

        itemDescription.setText(RAP.getInstance().itemName + "\n" + RAP.getInstance().insertedUPC);


    }

    public void returnToTakePhoto(View view){
        Intent intent = new Intent(view.getContext(), ActTakeItemPhoto.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }
    public void returnToInsertUPC(View view){
        Intent intent = new Intent(view.getContext(), ActInsertUPC.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }
    public void saveUPC(View view){
        //save image
        if(RAP.getInstance().categoryIds != ""){
            sendNewImage(view);
        }
        else {
            sendImage(view);
        }
        while(true){
            if(RAP.getInstance().saveItem()){
                break;
            }
        }
        if(RAP.getInstance().saveItem()){
            llConfirmUPC.setVisibility(View.GONE);
            llItemUPCsaved.setVisibility(View.VISIBLE);
        }

        //txtItemConfirmMessage.setText("Total data send: " + RAP.getInstance().totalDataSend + "\n Image size: " + Integer.toString(RAP.getInstance().imageDataTotal));
//        Intent intent = new Intent(view.getContext(), ActInsertUPC.class);
//        startActivityForResult(intent, Activity.RESULT_OK);
//        finish();
    }

    public void sendImage(View view){
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        Bitmap imageBitmap = BitmapFactory.decodeFile(PhotoPathHelper.getInstance().GetImagePath());
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
        byte[] imageByteArray = imageStream.toByteArray();
        RAP.getInstance().imageDataTotal = imageByteArray.length;

        String lookupUPCstring = "UPDATE|" + RAP.getInstance().prepareStringUPCcode(RAP.getInstance().insertedUPC)+ "|";
        byte[] lookupUPC = lookupUPCstring.getBytes(Charset.forName("UTF-16LE"));
        byte[] allData = new byte[lookupUPC.length + imageByteArray.length];
        System.arraycopy(lookupUPC, 0, allData, 0, lookupUPC.length);
        System.arraycopy(imageByteArray, 0, allData, lookupUPC.length, imageByteArray.length);

        divideAndSendImage(allData, view);
    }

    public void sendNewImage(View view){
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        Bitmap imageBitmap = BitmapFactory.decodeFile(PhotoPathHelper.getInstance().GetImagePath());
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageStream);
        byte[] imageByteArray = imageStream.toByteArray();
        RAP.getInstance().imageDataTotal = imageByteArray.length;

        String lookupUPCstring = "CREATE|" +
                RAP.getInstance().prepareStringUPCcode(RAP.getInstance().insertedUPC)+ "|" +
                RAP.getInstance().prepareStringName(RAP.getInstance().itemName) + "|" +
                RAP.getInstance().prepareStringCategory(RAP.getInstance().categoryIds) + "|";
        byte[] lookupUPC = lookupUPCstring.getBytes(Charset.forName("UTF-16LE"));
        byte[] allData = new byte[lookupUPC.length + imageByteArray.length];
        System.arraycopy(lookupUPC, 0, allData, 0, lookupUPC.length);
        System.arraycopy(imageByteArray, 0, allData, lookupUPC.length, imageByteArray.length);

        divideAndSendImage(allData, view);
    }

    private void divideAndSendImage(byte[] allData, View view){

        if(allData.length <= 1022){
            byte[] chunk = new byte[allData.length + 2];
            chunk[0] = (byte)0x01;
            chunk[1] = (byte)0x01;
            System.arraycopy(allData, 0, chunk, 2, allData.length);
            RAP.getInstance().imageChunk = chunk;
            RAP.getInstance().imageChunkList.add(chunk);
        }
        else{
            int numberOfChunks = ((allData.length - (allData.length % 1022)) / 1022) + 1;
            //int currentChunk = 1;
            RAP.getInstance().currentChunkToSend = 1;
            int currentSourceIndex = 0;
            int bytesRemaing = allData.length;
            if(!RAP.getInstance().imageChunkList.isEmpty())
                RAP.getInstance().imageChunkList.clear();
            for(int i = 0; i < numberOfChunks; i++){
                byte[] chunk;
                if(bytesRemaing < 1022){
                    chunk = new byte[bytesRemaing + 2];
                    chunk[0] = Byte.valueOf(String.valueOf(RAP.getInstance().currentChunkToSend));
                    chunk[1] = Byte.valueOf(String.valueOf(numberOfChunks));
                    System.arraycopy(allData, currentSourceIndex, chunk, 2, bytesRemaing);
                    bytesRemaing = 0;
                }
                else{
                    chunk = new byte[1024];
                    chunk[0] = Byte.valueOf(String.valueOf(RAP.getInstance().currentChunkToSend));
                    chunk[1] = Byte.valueOf(String.valueOf(numberOfChunks));
                    System.arraycopy(allData, currentSourceIndex, chunk, 2, 1022);
                    currentSourceIndex += 1022;
                    bytesRemaing -= 1022;
                }
                RAP.getInstance().currentChunkToSend++;

                RAP.getInstance().imageChunk = chunk;
                RAP.getInstance().imageChunkList.add(chunk);

            }
            WaitingForSaveImage waitingForSaveImage = new WaitingForSaveImage(getApplicationContext(), view.getRootView());
            waitingForSaveImage.execute("UPDATE");

        }
    }

}
