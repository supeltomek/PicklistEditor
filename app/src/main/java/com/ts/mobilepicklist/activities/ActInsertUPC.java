package com.ts.mobilepicklist.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.ts.mobilepicklist.R;
import com.ts.mobilepicklist.network.RAP;
import com.ts.mobilepicklist.network.WaitingForItem;
import com.ts.mobilepicklist.utils.CategoryDialog;
import com.ts.mobilepicklist.utils.CustomActivity;
import com.ts.mobilepicklist.utils.LayoutWorker;

public class ActInsertUPC extends CustomActivity {

    private EditText etInsertUPC;
    private TextView txtCategory;
    private String insertedUPC;
    private LinearLayout llInsertUPC;
    private LinearLayout llInsertUPCandCategory;
    private LinearLayout llLoadUPC;
    private int[] layoutIDS;
    //fields on insertUPC and Category
    private EditText txtItemName;
    private EditText etInsertedUPC;
    private Button categoryFieldBtn;

    //tmp
    private final static String DEBUG_TAG = "MobilePicklist";
    //CategoryExpandableLV listAdapter;


    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_insert_upc);

        etInsertUPC = (EditText) findViewById(R.id.txtUPC);
        //etInsertUPC.setRawInputType(Configuration.KEYBOARD_12KEY);

        txtItemName = (EditText) findViewById(R.id.txtItemName);
        etInsertedUPC = (EditText) findViewById(R.id.txtUPCinserted);
        categoryFieldBtn = (Button) findViewById(R.id.txtCategory);

        txtCategory = (TextView) findViewById(R.id.txtCategory);

        llInsertUPC = (LinearLayout) findViewById(R.id.llInsertUPC);
        llInsertUPCandCategory = (LinearLayout) findViewById(R.id.llInsertUPCandCategory);
        llLoadUPC = (LinearLayout) findViewById(R.id.llCheckingItemLoad);
        layoutIDS = new int[]{R.id.llInsertUPCandCategory, R.id.llCheckingItemLoad, R.id.llInsertUPC};

    }

    public void saveUPC(View view){
        insertedUPC = etInsertUPC.getText().toString();

        if(insertedUPC != null) {
            if (insertedUPC.length() > 0) {// || insertedUPC != null){
                Log.d("MobilePickList", "Check if upc exists: " + insertedUPC + ".");
                RAP.getInstance().insertedUPC = insertedUPC;
                //check if upc exists here
                //if(insertedUPC exists)
                WaitingForItem waitingForItem = new WaitingForItem(this.getApplicationContext(), this.getWindow().getDecorView().getRootView());
                waitingForItem.execute();
                while (true) {
                    if (RAP.getInstance().isItemPrepared) {
                        etInsertedUPC.setText(RAP.getInstance().insertedUPC);
                        break;
                    }
                }
            } else {
                Toast.makeText(this, "Insert UPC code.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Insert UPC code.", Toast.LENGTH_LONG).show();
        }
    }

    public void saveNewUPC(View view){
        if(etInsertedUPC.getText().length() == 0 | txtItemName.getText().length() == 0 | categoryFieldBtn.getText().length() == 0){
            Toast.makeText(getApplicationContext(), "Fill all data.", Toast.LENGTH_LONG).show();
        }
        else{
            RAP.getInstance().itemName = txtItemName.getText().toString();
            RAP.getInstance().itemUPCcode = etInsertedUPC.getText().toString();
            RAP.getInstance().categoryIds = RAP.getInstance().catListObj.getSelectedCategoryIds(RAP.getInstance().categoryList);
            Log.d("MobilePickList", "New Item, go to save confirmation window.");
            Intent intent = new Intent(view.getContext(), ActSaveItemConfirm.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.getApplicationContext().startActivity(intent);
        }
    }

    public void returnToInsUPC(View view){

        LayoutWorker.INSTANCE.showLayoutById(view.getRootView(), layoutIDS, R.id.llInsertUPC);
    }

    public void returnToTakePhoto(View view){
        Intent intent = new Intent(view.getContext(), ActTakeItemPhoto.class);
        startActivityForResult(intent, Activity.RESULT_OK);
        finish();
    }

    public void popupChooseCategory(View view){
        CategoryDialog cd = new CategoryDialog(ActInsertUPC.this, dialog, txtCategory);
        cd.showDialog(RAP.getInstance().categoryList);
    }


    @Override
    public void onDestroy(){
        if(dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
        super.onDestroy();
    }

}
