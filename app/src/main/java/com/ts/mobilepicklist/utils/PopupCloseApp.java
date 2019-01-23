package com.ts.mobilepicklist.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ts.mobilepicklist.R;

/**
 * Created by ts250231 on 2015-04-28.
 */
public class PopupCloseApp {

    private View view;
    private Context context;
    private PopupWindow popupWindow;
    private View popupView;

    public PopupCloseApp(View view, Context context){
        this.context = context;
        this.view = view;
    }

    public void DisplayPopup(){
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_closeapp, null);
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        ((Button)popupView.findViewById(R.id.btnPopupCloseAppYes)).setOnClickListener(popupCloseAppYes);
        ((Button)popupView.findViewById(R.id.btnPopupCloseAppNo)).setOnClickListener(popupCloseAppNo);
        ((LinearLayout)popupView.findViewById(R.id.popupCloseApp)).setOnClickListener(popupCloseAppNo);

        popupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener popupCloseAppYes = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            System.exit(0);
        }
    };
    private View.OnClickListener popupCloseAppNo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    };
}
