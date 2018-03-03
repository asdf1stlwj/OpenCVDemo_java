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
        myAdapter.getModel().add(new CommonData(CommandConstants.TEST_ENV_COMMAND,1));
        myAdapter.getModel().add(new CommonData(CommandConstants.MAT_PIXEL_INVERT_COMMAND,2));
        myAdapter.getModel().add(new CommonData(CommandConstants.BITMAP_PIXEL_INVERT_COMMAND,3));
        myAdapter.getModel().add(new CommonData(CommandConstants.BITMAP_PIXEL_SUBSTRACT_COMMAND,4));
        myAdapter.getModel().add(new CommonData(CommandConstants.BITMAP_PIXEL_ADD_COMMAND,5));
        myAdapter.getModel().add(new CommonData(CommandConstants.ADJUST_CONTRAST_COMMAND,6));
        myAdapter.getModel().add(new CommonData(CommandConstants.IMAGE_COIAINER_COMMAND,7));
        myAdapter.getModel().add(new CommonData(CommandConstants.SUB_IMAGE_COMMAND,8));
        myAdapter.getModel().add(new CommonData(CommandConstants.BLUR_IMAGE_COMMAND,9));
        myAdapter.getModel().add(new CommonData(CommandConstants.GUASSION_BLUR_COMMAND,10));
        myAdapter.getModel().add(new CommonData(CommandConstants.BI_BLUR_COMMAND,11));
        myAdapter.notifyDataSetChanged();

    }


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }

    private void processCommand() {
        Intent intent=new Intent(this,ProcessImageActivity.class);
        intent.putExtra("command",command);
        startActivity(intent);
    }
}

