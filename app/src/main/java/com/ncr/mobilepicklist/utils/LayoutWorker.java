package com.ncr.mobilepicklist.utils;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ncr.mobilepicklist.R;

/**
 * Created by ts250231 on 2015-06-23.
 */
public enum LayoutWorker {
    INSTANCE;
    public void showLayoutById(View view, int[] layoutIds, int layoutToEnable){
        LinearLayout linearLayout;
        for(int layoutid : layoutIds){
            linearLayout = (LinearLayout)view.findViewById(layoutid);
            Log.d("MobilePicklist", "Layout CategoryID: " + layoutid);
            try {
                if (layoutid == layoutToEnable) {

                    linearLayout.setVisibility(View.VISIBLE);
                    Log.d("MobilePicklist", "Show layout CategoryID: " + layoutid);
                }
                if (layoutid != layoutToEnable) {
                    linearLayout.setVisibility(View.GONE);
                    Log.d("MobilePicklist", "Disable layout CategoryID: " + layoutid);
                }
            }catch(Exception ex){
                Log.e("MobilePickList", "Error in layout worker: " + ex.getMessage());
            }
        }
    }
}
