package com.ncr.mobilepicklist.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ncr.mobilepicklist.R;
import com.ncr.mobilepicklist.network.RAP;

import java.util.List;

/**
 * Created by ts250231 on 2015-07-01.
 */
public class CategoryListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Category> values;
    private LayoutInflater inflator;
//    final int[] images = { R.drawable.magnolia,
//            R.drawable.orchid, R.drawable.rose,
//    };
    //String[] values = new String[]{"Test1", "Test2", "Test3", "Test4", "Test4", "Test5", "Test6"};
    public CategoryListAdapter(Context context, List<Category> values) {
        // TODO Auto-generated constructor stub
        this.mContext=context;
        this.values = values;
        this.inflator= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final MainListHolder mHolder;

        View v = convertView;
        if (convertView == null)
        {
            mHolder = new MainListHolder();

            v = inflator.inflate(R.layout.popup_category, null);
            mHolder.txtView = (TextView) v.findViewById(R.id.txtOnListItem);

            v.setTag(mHolder);
        } else {
            mHolder = (MainListHolder) v.getTag();
        }
        if(values.get(position).isSelected) {
            v.setBackgroundColor(Color.LTGRAY);
        }
        else {
            v.setBackgroundColor(Color.WHITE);
        }
        mHolder.txtView.setText(values.get(position).name);
        return v;
    }
    class MainListHolder
    {
        //private ImageView image;
        private TextView txtView;
    }




}