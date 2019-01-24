package com.ts.mobilepicklist.utils;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.ts.mobilepicklist.R;

/**
 * Created by ts250231 on 2015-04-28.
 */
public class PopupCategoryList {

    private View view;
    private Context context;
    private PopupWindow popupWindow;
    private View popupView;

    public PopupCategoryList(View view, Context context){
        this.context = context;
        this.view = view;
    }

    public void DisplayPopup(){
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_category_list, null);
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);


        popupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
    }


}
