package com.asdf.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
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
        //方法一:opencv自带方法
        Core.bitwise_not(src,src);

//        //方法二:opencv循环操作,耗时极长,不建议
//        int width=src.cols();
//        int height=src.rows();
//        int cnum=src.channels();
//        byte[] bgra= new byte[cnum];
//        for (int row=0;row<height;row++){
//            for (int col=0;col<width;col++){
//                src.get(row,col,bgra);//调用jni方法
//                for (int k=0;k<cnum;k++){
//                    bgra[k]= (byte) (256-bgra[k]&0Xff);
//                }
//                src.put(row,col,bgra);//调用jni方法
//            }
//        }
        Utils.matToBitmap(src,bitmap);
        src.release();
        return bitmap;
    }

    public static void localInvert(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int[] pixels=new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        int index=0;
        int a=0,r=0,g=0,b=0;
//        //方法一:用Color类
//        for (int i=0;i<pixels.length;i++){
//            int pixel=pixels[i];
//            a= Color.alpha(pixel);
//            r=Color.red(pixel);
//            g=Color.green(pixel);
//            b=Color.blue(pixel);
//            r=255-r;
//            g=255-g;
//            b=255-b;
//            pixels[i]=Color.argb(a,r,g,b);
//        }

        //方法二:java底层操作,Color类实际上是方法二的byte操作进行封装
        //  原理:或得每一个点的像素值(int),取反后的颜色值设置到对应的点上r = 255 - r;g = 255 - g;b = 255 - b;
        for (int row=0;row<height;row++){
            index=row*width;
            for (int col=0;col<width;col++){
                int pixel=pixels[index];
                a=(pixel>>24)&0xff;
                r=(pixel>>16)&0xff;
                g=(pixel>>8)&0xff;
                b=(pixel&0xff);
                r=255-r;
                g=255-g;
                b=255-b;
                pixel=((a&0xff)<<24)|((r&0xff)<<16)|((g&0xff)<<8)|(b&0xff);
                pixels[index]=pixel;
                index++;
            }
        }
        bitmap.setPixels(pixels,0,width,0,0,width,height);
    }

    public static void substract(Bitmap bitmap){
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat whiteImg=new Mat(src.size(),src.type(), Scalar.all(255));
        Core.subtract(whiteImg,src,src);
        Utils.matToBitmap(src,bitmap);
        src.release();
        whiteImg.release();
    }

    public static void add(Bitmap bitmap){
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat blackImg=new Mat(src.size(),src.type(), Scalar.all(0));
        Core.addWeighted(blackImg,0.5,src,0.5,0.0,src);
        Utils.matToBitmap(src,bitmap);
        src.release();
        blackImg.release();
    }
}
