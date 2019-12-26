package com.abneryan.segmentedbar;

import com.abneryan.segmentedbar.bean.IndicatorBean;

import java.util.List;

public interface SegemtedBarContract {
    interface View {
        void updateSegmentedDatas(List<IndicatorBean> dataList);
    }

    interface Presenter{
        void getSegmentedDatas();
    }
}
