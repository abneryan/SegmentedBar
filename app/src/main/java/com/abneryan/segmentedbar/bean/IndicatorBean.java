package com.abneryan.segmentedbar.bean;

import android.graphics.drawable.Drawable;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Keep
public class IndicatorBean {
    private String IndicatorValue;//指标值
    private String indicatorName;//指标名字
    private String indicatorUnit;
    private String indicatorResult;
    private String indicatorNote;
    private String indicatorDesc;
    private String indicatorKey;
    private String levelValue;//等级
    private String decription;//等级描述
    private Drawable cIcon;//变化图标
    private int bodyIcon;//身体图标
    private int indicatorLevel = -1;// 偏低，正常，偏高 三个颜色值的判断
    private boolean isClassifyBegain;
    private int classifyCount;
    private boolean isMinValue;
    private boolean isMaxValue;
    private HashMap<String, String> resultRangeMap;

    private ArrayList<Segment> segments = new ArrayList<>(); //分割段

    private ArrayList<Integer> segmentPointsColors = new ArrayList<>(); //分割点颜色

    public String getIndicatorUnit() {
        return indicatorUnit;
    }

    public void setIndicatorUnit(String indicatorUnit) {
        this.indicatorUnit = indicatorUnit;
    }

    public String getIndicatorValue() {
        return IndicatorValue;
    }

    public void setIndicatorValue(String indicatorValue) {
        IndicatorValue = indicatorValue;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public HashMap<String, String> getResultRangeMap() {
        return resultRangeMap;
    }

    public void setResultRangeMap(HashMap<String, String> resultRangeMap) {
        this.resultRangeMap = resultRangeMap;
    }

    private List<IndicatorResultRangeInfo> indicatorResultRanges;


    public String getIndicatorResult() {
        return indicatorResult;
    }

    public void setIndicatorResult(String indicatorResult) {
        this.indicatorResult = indicatorResult;
    }

    public String getIndicatorNote() {
        return indicatorNote;
    }

    public void setIndicatorNote(String indicatorNote) {
        this.indicatorNote = indicatorNote;
    }

    public String getIndicatorDesc() {
        return indicatorDesc;
    }

    public void setIndicatorDesc(String indicatorDesc) {
        this.indicatorDesc = indicatorDesc;
    }

    public String getIndicatorKey() {
        return indicatorKey;
    }

    public void setIndicatorKey(String indicatorKey) {
        this.indicatorKey = indicatorKey;
    }

    public List<IndicatorResultRangeInfo> getIndicatorResultRanges() {
        return indicatorResultRanges;
    }

    public void setIndicatorResultRanges(List<IndicatorResultRangeInfo> indicatorResultRanges) {
        this.indicatorResultRanges = indicatorResultRanges;
    }

    public int getIndicatorLevel() {
        return indicatorLevel;
    }

    public void setIndicatorLevel(int indicatorLevel) {
        this.indicatorLevel = indicatorLevel;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(String levelValue) {
        this.levelValue = levelValue;
    }

    public Drawable getcIcon() {
        return cIcon;
    }

    public void setcIcon(Drawable cIcon) {
        this.cIcon = cIcon;
    }

    public ArrayList<Segment> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }

    public ArrayList<Integer> getSegmentPointsColors() {
        return segmentPointsColors;
    }

    public void setSegmentPointsColors(ArrayList<Integer> segmentPointsColors) {
        this.segmentPointsColors = segmentPointsColors;
    }

    public int getBodyIcon() {
        return bodyIcon;
    }

    public void setBodyIcon(int bodyIcon) {
        this.bodyIcon = bodyIcon;
    }

    public boolean isClassifyBegain() {
        return isClassifyBegain;
    }

    public void setClassifyBegain(boolean classifyBegain) {
        isClassifyBegain = classifyBegain;
    }

    public int getClassifyCount() {
        return classifyCount;
    }

    public boolean isMinValue() {
        return isMinValue;
    }

    public void setMinValue(boolean minValue) {
        isMinValue = minValue;
    }

    public boolean isMaxValue() {
        return isMaxValue;
    }

    public void setMaxValue(boolean maxValue) {
        isMaxValue = maxValue;
    }

    public void setClassifyCount(int classifyCount) {
        this.classifyCount = classifyCount;
    }


    @Override
    public String toString() {
        return "UserDataBaseBean{" +
                "IndicatorValue='" + IndicatorValue + '\'' +
                ", indicatorName='" + indicatorName + '\'' +
                ", indicatorUnit='" + indicatorUnit + '\'' +
                ", indicatorResult='" + indicatorResult + '\'' +
                ", indicatorNote='" + indicatorNote + '\'' +
                ", indicatorDesc='" + indicatorDesc + '\'' +
                ", indicatorKey='" + indicatorKey + '\'' +
                ", levelValue='" + levelValue + '\'' +
                ", decription='" + decription + '\'' +
                ", indicatorLevel=" + indicatorLevel +
                ", bodyIcon=" + bodyIcon +
                ", classifyCount=" + classifyCount +
                ", isClassifyBegain=" + bodyIcon +
                ", isMinValue=" + isMinValue +
                ", isMaxValue=" + isMaxValue +
                '}';
    }
}
