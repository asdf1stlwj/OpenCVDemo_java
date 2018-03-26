package com.asdf.opencvpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.asdf.list.CommandConstants;
import com.asdf.list.CommonData;
import com.asdf.list.MyListViewAdapter;

public class MainActivity extends Activity {
    String command;
    MyListViewAdapter myAdapter;
    ListView lv_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadViewData();
    }

    private void loadViewData() {
        lv_items= (ListView) findViewById(R.id.lv_items);
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj=view.getTag();
                if (obj instanceof CommonData){
                    CommonData commonData= (CommonData) obj;
                    command=commonData.getCommand();
                }
                processCommand();
            }
        });
        myAdapter=new MyListViewAdapter(this);
        lv_items.setAdapter(myAdapter);
        myAdapter.getModel().addAll(CommonData.getCommonList());
        myAdapter.notifyDataSetChanged();

    }


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }

    private void processCommand() {
        if (command.equals(CommandConstants.THRESHOLD_BINARY_COMMAND)||
                command.equals(CommandConstants.ADAPTIVE_THRESHOLD_COMMAND)||
                command.equals(CommandConstants.ADAPTIVE_GAUSSIAN_COMMAND)||
                command.equals(CommandConstants.CANNY_EDGE_COMMAND)||
                command.equals(CommandConstants.HOUGH_LINE_COMMAND)||
                command.equals(CommandConstants.HOUGH_CIRCLE_COMMAND)||
                command.equals(CommandConstants.FIND_CONTOURS_COMMAND)||
                command.equals(CommandConstants.MEASURE_OBJECT_COMMAND)){
            Intent intent=new Intent(this,ThresholdProcessActivity.class);
            intent.putExtra("command",command);
            startActivity(intent);
        }else {
            Intent intent=new Intent(this,ProcessImageActivity.class);
            intent.putExtra("command",command);
            startActivity(intent);
        }

    }
}

