package com.asdf.opencvpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asdf.list.CommandConstants;
import com.asdf.utils.ImageProcessHelper;

import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThresholdProcessActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private String TAG="CVSAMPLE";
    private final int REQUEST_GET_IMAGE=1;
    private final int MAX_SIZE=1024;
    Button btn_process,btn_sel;
    ImageView iv_test;
    TextView tv_progress;
    SeekBar seekBar;
    Bitmap selectedBitmap;
    String commond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold_process);
        commond=getIntent().getStringExtra("command");
        btn_process = (Button) findViewById(R.id.btn_process);
        btn_sel= (Button) findViewById(R.id.btn_sel);
        seekBar= (SeekBar) findViewById(R.id.seekbar);
        tv_progress= (TextView) findViewById(R.id.tv_seekbarProgress);
        seekBar.setOnSeekBarChangeListener(this);
        btn_process.setTag("PROCESS");
        btn_process.setText(commond);
        btn_sel.setTag("SELECT");
        iv_test= (ImageView) findViewById(R.id.iv_test);
        btn_process.setOnClickListener(this);
        btn_sel.setOnClickListener(this);
        selectedBitmap=((BitmapDrawable) (iv_test.getDrawable())).getBitmap();
        initOpenCVLib();
    }

    private void initOpenCVLib() {
        boolean result= OpenCVLoader.initDebug();
        if (result==true){
            Log.i(TAG, "initOpenCVLib success");
        }else {
            Log.i(TAG, "initOpenCVLib fail");
        }
    }

    @Override
    public void onClick(View view) {
        Object obj=view.getTag();
        if (obj instanceof String){
            if (obj.equals("SELECT")){
                selectImage();
            }else if (obj.equals("PROCESS")){
                if (selectedBitmap==null){
                    Toast.makeText(this,"请选择一张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                processcommand(51);
//                Bitmap temp=selectedBitmap.copy(selectedBitmap.getConfig(),true);
//                if (commond.equals(CommandConstants.THRESHOLD_BINARY_COMMAND)){
//                    ImageProcessHelper.thresholdImg(commond,temp);
//                }else if(commond.equals(CommandConstants.ADAPTIVE_THRESHOLD_COMMAND)){
//                    ImageProcessHelper.adaptiveThresholdImg(51,false,temp);
//                }else if (commond.equals(CommandConstants.ADAPTIVE_GAUSSIAN_COMMAND)){
//                    ImageProcessHelper.adaptiveThresholdImg(51,true,temp);
//                }
//                if (temp!=null){
//                    iv_test.setImageBitmap(temp);
//                }
            }
        }
    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Brower Image..."),REQUEST_GET_IMAGE);
    }

    private void processcommand(int progress){
        if (progress%2==0){
            progress++;
        }
        Bitmap temp=selectedBitmap.copy(selectedBitmap.getConfig(),true);
        if (commond.equals(CommandConstants.THRESHOLD_BINARY_COMMAND)){
            ImageProcessHelper.manulThresholdImg(progress,temp);
        }else if(commond.equals(CommandConstants.ADAPTIVE_THRESHOLD_COMMAND)){
            ImageProcessHelper.adaptiveThresholdImg(progress,false,temp);
        }else if (commond.equals(CommandConstants.ADAPTIVE_GAUSSIAN_COMMAND)){
            ImageProcessHelper.adaptiveThresholdImg(progress,true,temp);
        }else if (commond.equals(CommandConstants.CANNY_EDGE_COMMAND)){
            ImageProcessHelper.cannyEdge(progress,temp);
        }else if (commond.equals(CommandConstants.HOUGH_LINE_COMMAND)){
            ImageProcessHelper.houghLine(progress,temp);
        }else if (commond.equals(CommandConstants.HOUGH_CIRCLE_COMMAND)){
            temp=ImageProcessHelper.houghCircle(progress,temp);
        }else if (commond.equals(CommandConstants.FIND_CONTOURS_COMMAND)){
            ImageProcessHelper.findAndDrawContours(progress,temp);
        }
        tv_progress.setText("当前阈值:"+progress);
        if (temp!=null){
            iv_test.setImageBitmap(temp);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_GET_IMAGE && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                BitmapFactory.decodeStream(inputStream,null,options);
                int width=options.outWidth;
                int height=options.outHeight;
                int samepleSize=1;
                int max=Math.max(width,height);
                if (max>MAX_SIZE){
                    int nwidth=width/2;
                    int nheight=height/2;
                    while (nwidth/samepleSize > MAX_SIZE || nheight/samepleSize>MAX_SIZE){
                        samepleSize*=2;
                    }
                }
                options.inSampleSize=samepleSize;
                options.inJustDecodeBounds=false;
                selectedBitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);
                iv_test.setImageBitmap(selectedBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage() );
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
         processcommand(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}