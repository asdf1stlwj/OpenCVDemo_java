package com.asdf.list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.asdf.opencvpro.R;
import com.asdf.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2018/2/6.
 */

public class MyListViewAdapter extends BaseAdapter {
    List<CommonData> commonDataList;
    Context context;

    public MyListViewAdapter(Context context){
        this.context=context;
        commonDataList=new ArrayList<>();
    }

    public List<CommonData> getModel(){
        return commonDataList;
    }

    @Override
    public int getCount() {
        return commonDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return commonDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return commonDataList.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view=LayoutInflater.from(context).inflate(R.layout.actlist_item,null);
        TextView tv_act= (TextView) view.findViewById(R.id.tv_act);
        final CommonData data=commonDataList.get(i);
        tv_act.setText(data.getCommand());
        view.setTag(data);
        return view;
    }
}
