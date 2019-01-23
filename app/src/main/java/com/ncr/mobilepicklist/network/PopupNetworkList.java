package com.ncr.mobilepicklist.network;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ncr.mobilepicklist.R;
import com.ncr.mobilepicklist.utils.StableArrayAdapter;

import java.util.ArrayList;

/**
 * Created by ts250231 on 2015-04-28.
 */
public class PopupNetworkList {

    private View view;
    private Context context;
    private Context baseContext;
    private PopupWindow popupWindow;
    private View popupView;

    private final static String DEBUG_TAG = "MobilePicklist";

    ArrayList<String> networkNames = new ArrayList<String>();


    public PopupNetworkList(View view, Context context, Context baseContext){
        this.context = context;
        this.baseContext = baseContext;
        this.view = view;
    }

    public void DisplayPopup(){
        LayoutInflater layoutInflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_listnetwork, null);
        popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        Log.d(DEBUG_TAG, "BEFORE PREPARE LIST DATA");
        prepareListData();
        Log.d(DEBUG_TAG, "AFTER PREPARE LIST DATA");

        ListView listView = (ListView)view.findViewById(R.id.networkListView);
        StableArrayAdapter adapter = new StableArrayAdapter(baseContext, R.layout.network_list_item, R.id.networkListItem, networkNames){
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(R.id.networkListItem);//(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);

                return view;
            }
        };

        popupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0, 0);
    }

    private void prepareListData() {
        networkNames.add("1");
        networkNames.add("2");
        networkNames.add("3");
    }
}
