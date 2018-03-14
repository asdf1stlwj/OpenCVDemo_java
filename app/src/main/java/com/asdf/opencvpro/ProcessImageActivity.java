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
import android.widget.Toast;

import com.asdf.list.CommandConstants;
import com.asdf.utils.ImageProcessHelper;

import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProcessImageActivity extends Activity implements View.OnClickListener{
    private String TAG="CVSAMPLE";
    private final int REQUEST_GET_IMAGE=1;
    private final int MAX_SIZE=1024;
    Button btn_process,btn_sel;
    ImageView iv_test;
    Bitmap selectedBitmap;
    String commond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);
        commond=getIntent().getStringExtra("command");
        btn_process = (Button) findViewById(R.id.btn_process);
        btn_sel= (Button) findViewById(R.id.btn_sel);
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
        boolean result=OpenCVLoader.initDebug();
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
                Bitmap temp=selectedBitmap.copy(selectedBitmap.getConfig(),true);
                if (commond.equals(CommandConstants.TEST_ENV_COMMAND)){
                    temp= ImageProcessHelper.convertToGray(temp);
                }else if (commond.equals(CommandConstants.MAT_PIXEL_INVERT_COMMAND)){
                    temp=ImageProcessHelper.invert(temp);
                }else if (commond.equals(CommandConstants.BITMAP_PIXEL_INVERT_COMMAND)){
                    ImageProcessHelper.localInvert(temp);
                }else if(commond.equals(CommandConstants.BITMAP_PIXEL_SUBSTRACT_COMMAND)){
                    ImageProcessHelper.substract(temp);
                }else if (commond.equals(CommandConstants.BITMAP_PIXEL_ADD_COMMAND)){
                    ImageProcessHelper.add(temp);
                }else if (commond.equals(CommandConstants.ADJUST_CONTRAST_COMMAND)){
                    ImageProcessHelper.adjustConstant(temp);
                }else if (commond.equals(CommandConstants.IMAGE_COIAINER_COMMAND)){
                    temp=ImageProcessHelper.demoMatUsage();
                }else if (commond.equals(CommandConstants.SUB_IMAGE_COMMAND)){
                    temp=ImageProcessHelper.getROIArea(temp);
                }else if (commond.equals(CommandConstants.BLUR_IMAGE_COMMAND)){
                    ImageProcessHelper.meanBlur(temp);
                }else if (commond.equals(CommandConstants.GUASSION_BLUR_COMMAND)){
                    ImageProcessHelper.gaussianBlur(temp);
                }else if (commond.equals(CommandConstants.BI_BLUR_COMMAND)){
                    ImageProcessHelper.biBlur(temp);
                }else if (commond.equals(CommandConstants.CUSTOM_BLUR_COMMAND)||
                          commond.equals(CommandConstants.CUSTOM_EDGE_COMMAND)||
                          commond.equals(CommandConstants.CUSTOM_SHARPEN_COMMAND)){
                    ImageProcessHelper.customFilter(commond,temp);
                }else if (commond.equals(CommandConstants.ERODE_COMMAND)||
                        commond.equals(CommandConstants.DILATE_COMMAND)){
                    ImageProcessHelper.erodeOrDilate(commond,temp);
                }else if (commond.equals(CommandConstants.OPEN_COMMAND)||
                        commond.equals(CommandConstants.CLOSE_COMMAND)){
                    ImageProcessHelper.openOrClose(commond,temp);
                }else if (commond.equals(CommandConstants.MORPH_LINE_COMMAND)){
                    ImageProcessHelper.morphLineDetection(temp);
                }else if (commond.equals(CommandConstants.THRESHOLD_BINARY_COMMAND)||
                          commond.equals(CommandConstants.THRESHOLD_BINARY_INV_COMMAND)||
                          commond.equals(CommandConstants.THRESHOLD_TRUNCAT_COMMAND)||
                          commond.equals(CommandConstants.THRESHOLD_TOZERO_COMMAND)||
                          commond.equals(CommandConstants.THRESHOLD_TOZERO_INV_COMMAND)){
                    ImageProcessHelper.thresholdImg(commond,temp);
                }else if (commond.equals(CommandConstants.HISTOGRAM_EQ_COMMAND)){
                    ImageProcessHelper.histogramEq(temp);
                }else if (commond.equals(CommandConstants.GRADIENT_SOBEL_X_COMMAND)){
                    ImageProcessHelper.sobleGradient(temp,1);
                }else if (commond.equals(CommandConstants.GRADIENT_SOBEL_Y_COMMAND)){
                    ImageProcessHelper.sobleGradient(temp,2);
                }
                if (temp!=null){
                    iv_test.setImageBitmap(temp);
                }
            }
        }
    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Brower Image..."),REQUEST_GET_IMAGE);
    }

//    private void commond_togray(){
//        Mat src=new Mat();
//        Mat dst=new Mat();
//        Bitmap temp=selectedBitmap.copy(selectedBitmap.getConfig(),true);
//        Utils.bitmapToMat(temp,src);
//        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
//        Utils.matToBitmap(dst,temp);
//        iv_test.setImageBitmap(temp);
//    }

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
}
