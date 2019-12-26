package com.abneryan.segmentedbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class IndicatorResultRangeInfo implements Parcelable {
    private String rangeName;
    private String rangeValue;

    public IndicatorResultRangeInfo()
    {

    }

    protected IndicatorResultRangeInfo(Parcel in) {
        rangeName = in.readString();
        rangeValue = in.readString();
    }

    public static final Creator<IndicatorResultRangeInfo> CREATOR = new Creator<IndicatorResultRangeInfo>() {
        @Override
        public IndicatorResultRangeInfo createFromParcel(Parcel in) {
            return new IndicatorResultRangeInfo(in);
        }

        @Override
        public IndicatorResultRangeInfo[] newArray(int size) {
            return new IndicatorResultRangeInfo[size];
        }
    };

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rangeName);
        parcel.writeString(rangeValue);
    }
}
