package com.asdf.list;

/**
 * Created by hasee on 2018/2/6.
 */

public interface CommandConstants {
    public static final String TEST_ENV_COMMAND="环境测试-灰度";
    public static final String MAT_PIXEL_INVERT_COMMAND ="Mat像素操作-取反";
    public static final String BITMAP_PIXEL_INVERT_COMMAND ="Bitmap像素操作-取反";
    public static final String BITMAP_PIXEL_SUBSTRACT_COMMAND ="Bitmap像素操作-减法";
    public static final String BITMAP_PIXEL_ADD_COMMAND ="Bitmap像素操作-加法";
    public static final String ADJUST_CONTRAST_COMMAND ="调整对比度和亮度";
    public static final String IMAGE_COIAINER_COMMAND ="图像容器-Mat";
    public static final String SUB_IMAGE_COMMAND ="图像容器-获取子图";
    public static final String BLUR_IMAGE_COMMAND ="均值模糊";
    public static final String GUASSION_BLUR_COMMAND ="高斯模糊";
    public static final String BI_BLUR_COMMAND ="双边模糊";
    public static final String CUSTOM_BLUR_COMMAND ="自定义算子-模糊";
    public static final String CUSTOM_EDGE_COMMAND ="自定义算子-边缘";
    public static final String CUSTOM_SHARPEN_COMMAND ="自定义算子-锐化";
    public static final String ERODE_COMMAND ="腐蚀-最小化滤波";
    public static final String DILATE_COMMAND ="膨胀-最大化滤波";
    public static final String OPEN_COMMAND ="开操作";
    public static final String CLOSE_COMMAND ="闭操作";
    public static final String MORPH_LINE_COMMAND ="形态学直线检测";
    public static final String THRESHOLD_BINARY_COMMAND ="阈值二值化";
    public static final String THRESHOLD_BINARY_INV_COMMAND ="阈值反二值化";
    public static final String THRESHOLD_TRUNCAT_COMMAND ="阈值截断";
    public static final String THRESHOLD_TOZERO_COMMAND ="阈值取零";
    public static final String THRESHOLD_TOZERO_INV_COMMAND ="阈值取零-反";
    public static final String ADAPTIVE_THRESHOLD_COMMAND ="自适应阈值-均值";
    public static final String ADAPTIVE_GAUSSIAN_COMMAND ="自适应阈值-高斯";
    public static final String HISTOGRAM_EQ_COMMAND ="直方图均衡化";
    public static final String GRADIENT_SOBEL_X_COMMAND ="图像梯度化-x方向";
    public static final String GRADIENT_SOBEL_Y_COMMAND ="图像梯度化-y方向";
    public static final String GRADIENT_IMG_COMMAND ="图像梯度化-xy方向";
    public static final String CANNY_EDGE_COMMAND ="Canny边缘提取";
    public static final String HOUGH_LINE_COMMAND ="霍夫直线提取";
    public static final String HOUGH_CIRCLE_COMMAND ="霍夫圆提取";
    public static final String TEMPLATE_MATCH_COMMAND ="模板匹配";
}
