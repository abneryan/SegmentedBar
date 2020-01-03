package com.abneryan.segmentedbar.utils;

import com.abneryan.segmentedbar.R;

import java.util.Arrays;
import java.util.List;

public class FatScalUtil {
    public static final int LOW_LEVEL_VALUE = -1;
    public static final int NORMAL_LEVEL_VALUE = 0;
    public static final int HIGH_LEVEL_VALUE = 1;
    public static final int STANDARD_COLOR_INDEX = 3;
    public static final String INDICATORRESULT_MIN_VALUE = "-1";
    public static final String INDICATORRESULT_MAX_VALUE = "99";

    public static final int[] SEGMENT_BG_COLORS = new int[]{
            R.color.color_330292, R.color.color_0025FE, R.color.color_05BCFF,
            R.color.color_18D81E, R.color.color_FEA141, R.color.color_F62408,
            R.color.color_A10010
    };

    public static final List<String> STANDARD_KEYS = Arrays.asList(
            "年轻","正常","标准","优","达标","理想","健康",
            "偏高（优秀）","偏高(优)", "健康型","标准（健康型）", "标准（警戒型）",
            "标准型","模特型");


    public static int getBodyIconResourceId(int level) {
        int resourceId = -1;
        if(level == LOW_LEVEL_VALUE){
            resourceId = R.mipmap.icon_man_thin;
        } else if(level == NORMAL_LEVEL_VALUE){
            resourceId = R.mipmap.icon_man;
        } else if(level == HIGH_LEVEL_VALUE){
            resourceId = R.mipmap.icon_man_fat;
        }
        return resourceId;
    }

    public static int getLevelBgResource(int level){
        int resourceId = -1;
        switch (level){
            case LOW_LEVEL_VALUE:
                resourceId=  R.drawable.blue_corner_background;
                break;
            case NORMAL_LEVEL_VALUE:
                resourceId =  R.drawable.green_corner_background;
                break;
            case HIGH_LEVEL_VALUE:
                resourceId =  R.drawable.red_corner_background;
                break;
        }
       return resourceId;
    }
}
