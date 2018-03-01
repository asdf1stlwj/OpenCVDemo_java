package com.asdf.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
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

    /**
     * 像素点减法,减去一张白色图片rgb(255,255,255)相当于减少亮度
     * @param bitmap
     */
    public static void substract(Bitmap bitmap){
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat whiteImg=new Mat(src.size(),src.type(), Scalar.all(255));
        Core.subtract(whiteImg,src,src);//相当于取反,255-whiteImg嘛
        Utils.matToBitmap(src,bitmap);
        src.release();
        whiteImg.release();
    }

    /**
     * 像素点加法,加一张白色图片rgb(255,255,255)相当于增加亮度,加减一张黑色图片rgb(0,0,0)没有任何变化
     * @param bitmap
     */
    public static void add(Bitmap bitmap){
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat blackImg=new Mat(src.size(),src.type(), Scalar.all(0));
        Core.addWeighted(blackImg,0.5,src,0.5,0.0,src);
        Utils.matToBitmap(src,bitmap);
        src.release();
        blackImg.release();
    }

    /**
     * 像素点乘法,相当于改变对比度,例如放大后像素点之间的差值越来越大,因此对比度加大,反之亦然
     * @param bitmap
     */
    public static void adjustConstant(Bitmap bitmap){
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        src.convertTo(src, CvType.CV_32F);
        Mat bwImg=new Mat(src.size(),src.type(),Scalar.all(0.6));
        Mat whiteImg=new Mat(src.size(),src.type(),Scalar.all(30));
        Core.multiply(bwImg,src,src);
        Core.add(src,whiteImg,src);
        src.convertTo(src, CvType.CV_8U);
        Utils.matToBitmap(src,bitmap);
        src.release();
        bwImg.release();
        whiteImg.release();
    }

    public static Bitmap demoMatUsage(){
//        Mat src=new Mat();
//        Utils.bitmapToMat(bitmap,src);
//        Mat dst=new Mat(src.size(),src.type(),Scalar.all(127));
        Bitmap bitmap=Bitmap.createBitmap(400,600, Bitmap.Config.ARGB_8888);
        //CV_8UC3:8位unsigned char 3通道
        //注意这个函数里是N通道的话，Scalar就输N个参数
        Mat dst=new Mat(bitmap.getHeight(),bitmap.getWidth(),CvType.CV_8UC3,Scalar.all(127));
        Utils.matToBitmap(dst,bitmap);
        dst.release();
        return bitmap;
    }

    public static Bitmap getROIArea(Bitmap bitmap){
        Rect roi=new Rect(200,400,300,600);
        Bitmap roiBitmap=Bitmap.createBitmap(roi.width,roi.height, Bitmap.Config.ARGB_8888);
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat roiMat=src.submat(roi);
        Mat roiDstMat=new Mat();
        Imgproc.cvtColor(roiMat,roiDstMat,Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(roiDstMat,roiBitmap);
        src.release();
        roiMat.release();
        roiDstMat.release();
        return roiBitmap;
    }
}
