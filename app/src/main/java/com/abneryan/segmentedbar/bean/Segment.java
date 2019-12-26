package com.abneryan.segmentedbar.bean;

import androidx.annotation.Keep;

@Keep
public class Segment {
    private String descriptionText;                      //描述信息
    private int color;                                   //分段控件颜色
    private float minValue = -1;                         //分段控件的起始值（start）
    private float maxValue = -1;                         //分段控件的结束值(end)

    public Segment() {
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "descriptionText='" + descriptionText + '\'' +
                ", color=" + color +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}
