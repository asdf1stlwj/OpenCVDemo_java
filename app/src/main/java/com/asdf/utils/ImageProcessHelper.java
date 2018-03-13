package com.asdf.utils;

import android.graphics.Bitmap;

import com.asdf.list.CommandConstants;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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

    //均值模糊
    public static void meanBlur(Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //这里size类中width为1代表纵向模糊,类似上下快速抖动(社保),height同理
        Imgproc.blur(src,dst,new Size(15,15),new Point(-1,-1),Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    //高斯模糊
    public static void gaussianBlur(Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //这里的Size参数可以设为(0,0),此时由后面的sigmaX,sigmaY决定滤波器的值系数,若size参数有指定,
        //则无视后面的sigmaX,sigmaY(实际上有一个固定的公式决定)
        //另外这个Size参数必须为奇数,否则有可能会报错
        Imgproc.GaussianBlur(src,dst,new Size(15,15),0,0,Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    //双边模糊
    public static void biBlur(Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2BGR);//4通道(ARGB)转3通道(RGB)
        Imgproc.bilateralFilter(src,dst,15,150,15,Imgproc.BORDER_DEFAULT);//该函数只认单通道和三通道
        Mat kernel=new Mat(3,3,CvType.CV_16S);
        kernel.put(0,0,0,-1,0,-1,5,-1,0,-1,0);
        Imgproc.filter2D(dst,dst,-1,kernel,new Point(-1,-1),0.0,Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        kernel.release();
    }

    //自定义算子应用
    public static void customFilter(String commond,Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat kernel=getCustomFilter(commond);
        Imgproc.filter2D(src,dst,-1,kernel,new Point(-1,-1),0.0,Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        kernel.release();
    }

    private static Mat getCustomFilter(String commond) {
        Mat kernel=new Mat(3,3,CvType.CV_32FC1);
        if (commond.equals(CommandConstants.CUSTOM_BLUR_COMMAND)){
            //前两个参数：开始的中心点,现在是设置为从(0,0)开始,后面的参数为算子设置,前面设置是多少这里就输入多少个参数
            //例如现在的参数是3*3=9,输入9个参数
            kernel.put(0,0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0);
        }else if (commond.equals(CommandConstants.CUSTOM_EDGE_COMMAND)){
            //拉普拉斯边缘
            kernel.put(0,0,-1,-1,-1,-1,8,-1,-1,-1,-1);
        }else if (commond.equals(CommandConstants.CUSTOM_SHARPEN_COMMAND)){
            //拉普拉斯锐化
            kernel.put(0,0,-1,-1,-1,-1,9,-1,-1,-1,-1);
        }
        return kernel;
    }

    //自定义算子应用
    public static void erodeOrDilate(String commond,Bitmap bitmap){
        boolean erode=commond.equals(CommandConstants.ERODE_COMMAND);
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Mat structElement=Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,new Size(3,3),new Point(-1,-1));
        if (erode){
            Imgproc.erode(src,dst,structElement,new Point(-1,-1),1);
        }else {
            Imgproc.dilate(src,dst,structElement,new Point(-1,-1),1);
        }
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        structElement.release();
    }

    //开闭操作
    //开操作:先腐蚀后膨胀
    //闭操作:先膨胀后腐蚀
    public static void openOrClose(String commond,Bitmap bitmap){
        boolean open=commond.equals(CommandConstants.OPEN_COMMAND);
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(src,src,0,255,Imgproc.THRESH_BINARY_INV |Imgproc.THRESH_OTSU);
        Mat structElement=Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT,new Size(3,3),new Point(-1,-1));
        if (open){
            Imgproc.morphologyEx(src,dst,Imgproc.MORPH_OPEN,structElement);
        }else {
            Imgproc.morphologyEx(src,dst,Imgproc.MORPH_CLOSE,structElement);
        }
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        structElement.release();
    }

    //形态学直线检测
    //关键要选择合适的结构体(例如检测直线需要长方形的结构体)
    public static void morphLineDetection(Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);
        Imgproc.threshold(src,src,0,255,Imgproc.THRESH_BINARY_INV |Imgproc.THRESH_OTSU);
        Mat structElement=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(30,1),new Point(-1,-1));
        Imgproc.morphologyEx(src,dst,Imgproc.MORPH_OPEN,structElement);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        structElement.release();
    }

    public static void manulThresholdImg(int progress,Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //注意先把图像转为灰度图像,才再把灰度图像转为二值化图像(其实就是只有黑白：0和255)
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);
        //Imgproc.THRESH_OTSU:代表根据opencv自带算法求出阈值（下面第三个参数）
        //第四个参数:代表最大值
        Imgproc.threshold(src,dst,progress,255,Imgproc.THRESH_BINARY);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    public static void thresholdImg(String command,Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //注意先把图像转为灰度图像,才再把灰度图像转为二值化图像(其实就是只有黑白：0和255)
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);
        //Imgproc.THRESH_OTSU:代表根据opencv自带算法求出阈值（下面第三个参数）
        //第四个参数:代表最大值
        Imgproc.threshold(src,dst,0,255,getType(command));
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }

    public static int getType(String command){
        if (command.equals(CommandConstants.THRESHOLD_BINARY_COMMAND)){
            return Imgproc.THRESH_BINARY |Imgproc.THRESH_OTSU;
        }else if (command.equals(CommandConstants.THRESHOLD_BINARY_INV_COMMAND)){
            return Imgproc.THRESH_BINARY_INV |Imgproc.THRESH_OTSU;
        }else if (command.equals(CommandConstants.THRESHOLD_TRUNCAT_COMMAND)){
            return Imgproc.THRESH_TRUNC|Imgproc.THRESH_OTSU;
        }else if (command.equals(CommandConstants.THRESHOLD_TOZERO_COMMAND)){
            return Imgproc.THRESH_TOZERO|Imgproc.THRESH_OTSU;
        }else if (command.equals(CommandConstants.THRESHOLD_TOZERO_INV_COMMAND)){
            return Imgproc.THRESH_TOZERO_INV|Imgproc.THRESH_OTSU;
        }else {
            return Imgproc.THRESH_BINARY |Imgproc.THRESH_OTSU;
        }
    }

    /**
     * 自适应阈值,当一张图片受光照区域不均全局阈值往往失,此时就应该用自适应阈值
     * @param progress 块越大区分度越高
     * @param isGaosi 均值或高斯
     * @param bitmap
     */
    public static void adaptiveThresholdImg(int progress,boolean isGaosi,Bitmap bitmap){
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //注意先把图像转为灰度图像,才再把灰度图像转为二值化图像(其实就是只有黑白：0和255)
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2GRAY);
        //Imgproc.THRESH_OTSU:代表根据opencv自带算法求出阈值（下面第三个参数）
        //第四个参数:代表最大值
        Imgproc.adaptiveThreshold(src,dst,255,isGaosi?Imgproc.ADAPTIVE_THRESH_MEAN_C:Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                     Imgproc.THRESH_BINARY,progress,0.0);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
}
