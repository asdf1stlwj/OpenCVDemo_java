package com.asdf.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2018/2/6.
 */

public class CommonData implements CommandConstants{
    private long id;
    private String command;
    private String name;

    public CommonData(String command,long id){
        this.command=command;
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static List<CommonData> getCommonList(){
        List<CommonData> commonDatas=new ArrayList<>();
        commonDatas.add(new CommonData(TEST_ENV_COMMAND,1));
        commonDatas.add(new CommonData(MAT_PIXEL_INVERT_COMMAND,2));
        commonDatas.add(new CommonData(BITMAP_PIXEL_INVERT_COMMAND,3));
        commonDatas.add(new CommonData(BITMAP_PIXEL_SUBSTRACT_COMMAND,4));
        commonDatas.add(new CommonData(BITMAP_PIXEL_ADD_COMMAND,5));
        commonDatas.add(new CommonData(ADJUST_CONTRAST_COMMAND,6));
        commonDatas.add(new CommonData(IMAGE_COIAINER_COMMAND,7));
        commonDatas.add(new CommonData(SUB_IMAGE_COMMAND,8));
        commonDatas.add(new CommonData(BLUR_IMAGE_COMMAND,9));
        commonDatas.add(new CommonData(GUASSION_BLUR_COMMAND,10));
        commonDatas.add(new CommonData(BI_BLUR_COMMAND,11));
        commonDatas.add(new CommonData(CUSTOM_BLUR_COMMAND,12));
        commonDatas.add(new CommonData(CUSTOM_EDGE_COMMAND,13));
        commonDatas.add(new CommonData(CUSTOM_SHARPEN_COMMAND,14));
        commonDatas.add(new CommonData(ERODE_COMMAND,15));
        commonDatas.add(new CommonData(DILATE_COMMAND,16));
        commonDatas.add(new CommonData(OPEN_COMMAND,17));
        commonDatas.add(new CommonData(CLOSE_COMMAND,18));
        commonDatas.add(new CommonData(MORPH_LINE_COMMAND,19));
        commonDatas.add(new CommonData(THRESHOLD_BINARY_COMMAND,20));
        commonDatas.add(new CommonData(THRESHOLD_BINARY_INV_COMMAND,21));
        commonDatas.add(new CommonData(THRESHOLD_TRUNCAT_COMMAND,22));
        commonDatas.add(new CommonData(THRESHOLD_TOZERO_COMMAND,23));
        commonDatas.add(new CommonData(THRESHOLD_TOZERO_INV_COMMAND,24));
        commonDatas.add(new CommonData(ADAPTIVE_THRESHOLD_COMMAND,25));
        commonDatas.add(new CommonData(ADAPTIVE_GAUSSIAN_COMMAND,26));
        commonDatas.add(new CommonData(HISTOGRAM_EQ_COMMAND,27));
        commonDatas.add(new CommonData(GRADIENT_SOBEL_X_COMMAND,28));
        commonDatas.add(new CommonData(GRADIENT_SOBEL_Y_COMMAND,29));
        commonDatas.add(new CommonData(GRADIENT_IMG_COMMAND,30));
        commonDatas.add(new CommonData(CANNY_EDGE_COMMAND,31));
        commonDatas.add(new CommonData(HOUGH_LINE_COMMAND,32));
        commonDatas.add(new CommonData(HOUGH_CIRCLE_COMMAND,33));
        commonDatas.add(new CommonData(TEMPLATE_MATCH_COMMAND,34));
        return commonDatas;
    }
}
