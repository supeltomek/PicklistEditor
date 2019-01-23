package com.ncr.mobilepicklist.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ncr.mobilepicklist.R;
import com.ncr.mobilepicklist.network.RAP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts250231 on 2015-07-01.
 */
public class CategoryDialog {

    AlertDialog dialog;
    Context context;
    TextView txtView;
    long lastClickTime = 0;
    boolean clickedLong = false;
    List<Category> currentCategoryList = new ArrayList<Category>();

    public CategoryDialog(Context context, AlertDialog dialog, TextView textViewToChange){
        this.dialog = dialog;
        this.context = context;
        this.txtView = textViewToChange;
    }


    public void showDialog(final List<Category> subCategoryList){
        try {
            lastClickTime = 0;
            currentCategoryList = subCategoryList;
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Category list:");
            ListView list=new ListView(context);
            list.setAdapter(new CategoryListAdapter(context, subCategoryList));
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!subCategoryList.get(i).isSelected) {
                        view.setBackgroundColor(Color.LTGRAY);
                        subCategoryList.get(i).isSelected = true;
                    }
                    else {
                        view.setBackgroundColor(Color.WHITE);
                        subCategoryList.get(i).isSelected = true;
                    }

                    clickedLong = true;
                    return false;
                }
            });
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    if(!clickedLong){
                        if(dialog.isShowing())
                        {
                            if(subCategoryList.get(position).subCategory.size() > 0) {
                                dialog.dismiss();
                                showDialog(subCategoryList.get(position).subCategory);
                            }
                        }
                    }
                    clickedLong = false;

                }
            });
            builder.setView(list);
            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                        txtView.setText(RAP.getInstance().catListObj.getSelectedCategoryName(RAP.getInstance().categoryList));
                    }
                }
            });
            builder.setNegativeButton("UP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    currentCategoryList = RAP.getInstance().catListObj.getParentListByUniqIdChild(currentCategoryList.get(0).uniqueID, RAP.getInstance().categoryList);

                    if(currentCategoryList.size() > 0) {
                        dialog.dismiss();
                        showDialog(currentCategoryList);
                    }
                }
            });
            dialog=builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            dialog.setCanceledOnTouchOutside(true);

            dialog.show();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
