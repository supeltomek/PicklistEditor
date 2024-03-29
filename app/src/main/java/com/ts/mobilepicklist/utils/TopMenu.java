package com.ts.mobilepicklist.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ts.mobilepicklist.R;

/**
 * Created by ts250231 on 2015-04-27.
 */
public class TopMenu extends LinearLayout {

    private LayoutInflater inflater;
    private Context context;
    public TopMenu(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_menu, this, true);

        ((Button)this.findViewById(R.id.closeBtnInLayout)).setOnClickListener(closeApplication);

    }

    private OnClickListener closeApplication = new OnClickListener() {
        @Override
        public void onClick(View view) {
//            ((Activity)getContext()).finish();
//            System.exit(0);
            PopupCloseApp popupCloseApp = new PopupCloseApp(view.getRootView(), context.getApplicationContext());// getWindow().getDecorView().getRootView(), this.getApplicationContext());
            popupCloseApp.DisplayPopup();
        }
    };
}
