package com.asdf.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by hasee on 2018/2/8.
 */

public class ImageProcessHelper {
    private static String TAG="ImageProcessHelper";
    public static Bitmap convertToGray(Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        return bitmap;
    }

    public static Bitmap invert(Bitmap bitmap){

        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        int width=src.cols();
        int height=src.rows();
        int cnum=src.channels();
        byte[] bgra= new byte[cnum];
        for (int row=0;row<height;row++){
            for (int col=0;col<width;col++){
                src.get(row,col,bgra);
                for (int k=0;k<cnum;k++){
                    bgra[k]= (byte) (256-bgra[k]&0Xff);
                }
                src.put(row,col,bgra);
            }
        }
        Utils.matToBitmap(src,bitmap);
        src.release();
        return bitmap;
    }
}
