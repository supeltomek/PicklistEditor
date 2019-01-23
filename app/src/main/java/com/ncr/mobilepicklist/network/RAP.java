package com.ncr.mobilepicklist.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;

import com.ncr.mobilepicklist.utils.Category;
import com.ncr.mobilepicklist.utils.CategoryList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ts250231 on 2015-04-29.
 */
public class RAP {

    private static RAP instance;
    private Context context;
    private View view;
    private String RapIP;
    public Camera cameraDevice = null;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public RapTcpClient mRapTcpClient;
    public boolean isConnectedToRap = false;
    public boolean comunicationError = false;
    public boolean isItemPrepared = false;
    public String insertedUPC;
    public byte[] imageChunk = new byte[1024];
    public ArrayList<byte[]> imageChunkList = new ArrayList<byte[]>();
    public boolean imageSavedStatus = false;
    public boolean imageWasTaken = false;
    public boolean imageChunkWasSend = false;
    public int currentChunkToSend;
    public String totalDataSend;
    public int imageDataTotal;


    DataOutputStream out;
    DataInputStream in;

    public List<Category> categoryList = new ArrayList<Category>();
    public CategoryList catListObj;
    public String itemName;
    public String itemUPCcode;
    public String categoryIds = "";

    public static RAP getInstance(){
        if(instance == null){
            //init();
            instance = new RAP();
        }
        return instance;
    }

    public RAP(){
        mRapTcpClient = new RapTcpClient();
    }

    public boolean isVisible(){
        return false;
    }



    public void setRapIP(Context context, String IP){
        this.context = context;
        this.view = view;
        settings = context.getSharedPreferences("PickListEditorSettings", 0);
        editor = settings.edit();

        RapIP = IP;
        editor.putString("RAPIP", IP);
        editor.commit();
    }

    public String getRapIP(){
        if(settings != null)
            RapIP = settings.getString("RAPIP", "").toString();
        return RapIP;
    }



    public boolean saveItem(){
        if(imageSavedStatus) {
            return true;
        }
        return false;
    }

    public String prepareStringUPCcode(String upc){
        char[] upcCharArray = upc.toCharArray();
        char[] tmp = new char[30];
        Arrays.fill(tmp, ' ');

        if(upc.length()<30){
            System.arraycopy(upcCharArray,0 , tmp, tmp.length - upcCharArray.length, upcCharArray.length);
        }
        String preparedItem = String.valueOf(tmp);
        return preparedItem;
    }
    public String prepareStringName(String name){
        char[] nameCharArray = name.toCharArray();
        char[] tmp = new char[50];
        Arrays.fill(tmp, ' ');

        if(name.length()<50){
            System.arraycopy(nameCharArray,0 , tmp, tmp.length - nameCharArray.length, nameCharArray.length);
        }
        String preparedItem = String.valueOf(tmp);
        return preparedItem;
    }
    public String prepareStringCategory(String categoryIds){
        char[] categoryCharArray = categoryIds.toCharArray();
        char[] tmp = new char[20];
        Arrays.fill(tmp, ' ');

        if(categoryIds.length()<20){
            System.arraycopy(categoryCharArray,0 , tmp, tmp.length - categoryCharArray.length, categoryCharArray.length);
        }
        String preparedItem = String.valueOf(tmp);
        return preparedItem;
    }

    public void convertMessageFromRap(String message){

        if(message.contains("HELLO")){
            Log.d("MobilePickList", "Receive message HELLO.");
            prepareCategoryList(message);
        }
        else if(message.contains("LOOKUP")){
            Log.d("MobilePickList", "Received message LOOKUP");
            prepareItemInfo(message);
        }
        else if(message.contains("UPDATE")){
            Log.d("MobilePickList", "Received message UPDATE");
            if(message.contains("ACK")){
                imageSavedStatus = true;
            }
            else if(message.contains("NAK")){
                imageSavedStatus = false;
            }
        }
        else if(message.contains("CREATE")){
            Log.d("MobilePickList", "Received message CREATE");
            if(message.contains("ACK")){
                imageSavedStatus = true;
            }
            else if(message.contains("NAK")){
                imageSavedStatus = false;
            }
        }


    }

    private void prepareItemInfo(String itemInfoFromServer){

        itemUPCcode = RAP.getInstance().insertedUPC;
        itemName = itemInfoFromServer.substring(7, itemInfoFromServer.indexOf(0));
        isItemPrepared = true;
    }
    private void prepareCategoryList(String catagoriesString){

        categoryList.clear();

        String categories = catagoriesString.substring(6);

        catListObj = new CategoryList(categories);
        categoryList = catListObj.categoryList;

    }


}
