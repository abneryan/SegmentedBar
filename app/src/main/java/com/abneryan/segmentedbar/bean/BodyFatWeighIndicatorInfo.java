package com.abneryan.segmentedbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class BodyFatWeighIndicatorInfo implements Parcelable {
    private String indicatorName;
    private String indicatorKey;
    private String indicatorValue;
    private String indicatorResult;
    private String indicatorUnit;
    private String indicatorNote;
    private String indicatorDesc;
    private List<IndicatorResultRangeInfo> indicatorResultRange;
    private List<String> indicatorValueRange;

    public List<String> getIndicatorValueRange() {
        return indicatorValueRange;
    }

    public void setIndicatorValueRange(List<String> indicatorValueRange) {
        this.indicatorValueRange = indicatorValueRange;
    }

    protected BodyFatWeighIndicatorInfo(Parcel in) {
        indicatorName = in.readString();
        indicatorKey = in.readString();
        indicatorValue = in.readString();
        indicatorResult = in.readString();
        indicatorUnit = in.readString();
        indicatorNote = in.readString();
        indicatorDesc = in.readString();
        if (indicatorResultRange == null) {
            indicatorResultRange = new ArrayList<>();
        }
        in.readTypedList(indicatorResultRange, IndicatorResultRangeInfo.CREATOR);
        indicatorValueRange = in.createStringArrayList();
    }

    public BodyFatWeighIndicatorInfo()
    {

    }

    public static final Creator<BodyFatWeighIndicatorInfo> CREATOR = new Creator<BodyFatWeighIndicatorInfo>() {
        @Override
        public BodyFatWeighIndicatorInfo createFromParcel(Parcel in) {
            return new BodyFatWeighIndicatorInfo(in);
        }

        @Override
        public BodyFatWeighIndicatorInfo[] newArray(int size) {
            return new BodyFatWeighIndicatorInfo[size];
        }
    };

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getIndicatorKey() {
        return indicatorKey;
    }

    public void setIndicatorKey(String indicatorKey) {
        this.indicatorKey = indicatorKey;
    }

    public String getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(String indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    public String getIndicatorResult() {
        return indicatorResult;
    }

    public void setIndicatorResult(String indicatorResult) {
        this.indicatorResult = indicatorResult;
    }

    public String getIndicatorUnit() {
        return indicatorUnit;
    }

    public void setIndicatorUnit(String indicatorUnit) {
        this.indicatorUnit = indicatorUnit;
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

    public List<IndicatorResultRangeInfo> getIndicatorResultRange() {
        return indicatorResultRange;
    }

    public void setIndicatorResultRange(List<IndicatorResultRangeInfo> indicatorResultRange) {
        this.indicatorResultRange = indicatorResultRange;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(indicatorName);
        parcel.writeString(indicatorKey);
        parcel.writeString(indicatorValue);
        parcel.writeString(indicatorResult);
        parcel.writeString(indicatorUnit);
        parcel.writeString(indicatorNote);
        parcel.writeString(indicatorDesc);
        parcel.writeTypedList(indicatorResultRange);
        parcel.writeStringList(indicatorValueRange);
    }
}
