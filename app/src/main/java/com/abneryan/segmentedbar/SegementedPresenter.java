package com.abneryan.segmentedbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.JsonToken;

import androidx.annotation.NonNull;

import com.abneryan.segmentedbar.bean.BodyFatWeighIndicatorInfo;
import com.abneryan.segmentedbar.bean.IndicatorBean;
import com.abneryan.segmentedbar.bean.IndicatorResultRangeInfo;
import com.abneryan.segmentedbar.bean.Segment;
import com.abneryan.segmentedbar.utils.FatScalUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SegementedPresenter implements SegemtedBarContract.Presenter {
    //体重,BMI,脂肪率,肌肉率,水分率,基础代谢,内脏脂肪,蛋白质,骨量,身体年龄,体型,去脂体重,脂肪量,肌肉量,骨骼肌,肌肉控制,脂肪控制
    //骨骼肌率,皮下脂肪率,蛋白质总量,水分重量,燃脂心率次数,营养状态,标准体重,理想体重,肥胖水平,体重控制量

    String[] keyArray = new String[]{
            "weight", "bmi", "pbf", "musr", "tbw", "bm", "vfi", "protein",
            "bone", "ba", "shape", "lbm", "fm", "mus", "smm", "mc", "fc",
            "ratioOfSkeletalMuscle","ratioOfSubcutaneousFatRange","weightOfProtein",
            "weightOfWater","rateOfBurnFat","stateOfNutrition","desirableWeight","idealWeight",
            "obesityLevel","weightToControl"
    };
    private SegemtedBarContract.View mView;
    private Context mContext;

    public SegementedPresenter(SegemtedBarContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getSegmentedDatas() {
        String indicatorJson = mContext.getResources().getString(R.string.indicator_json2);
        List<BodyFatWeighIndicatorInfo> list = new Gson().fromJson(indicatorJson,new TypeToken<ArrayList<BodyFatWeighIndicatorInfo>>(){}.getType());

        List<IndicatorBean> segmentedDataList = getBodyReportDataList(list);
        mView.updateSegmentedDatas(segmentedDataList);
    }

    @NonNull
    private List getBodyReportDataList( List<BodyFatWeighIndicatorInfo> indicatorInfos) {
        if(indicatorInfos == null){
            return null;
        }
        List<IndicatorBean> segmentedDataList = new ArrayList<>();

        HashMap<String,BodyFatWeighIndicatorInfo> fatWeightIndicatorInfoMap = new HashMap<>();
        if(indicatorInfos!=null && indicatorInfos.size()>0){
            for(BodyFatWeighIndicatorInfo bodyFatWeighIndicatorInfo : indicatorInfos){
                if(bodyFatWeighIndicatorInfo!=null){
                    fatWeightIndicatorInfoMap.put(bodyFatWeighIndicatorInfo.getIndicatorKey(),bodyFatWeighIndicatorInfo);
                }
            }
        }

        //测试报告列表：体重,BMI,脂肪率,肌肉率,水分率,基础代谢,内脏脂肪,蛋白质,骨量,身体年龄,体型,去脂体重,脂肪量,肌肉量,骨骼肌,肌肉控制,脂肪控制
        //骨骼肌率,皮下脂肪率,蛋白质总量,水分重量,燃脂心率次数,营养状态,标准体重,理想体重,肥胖水平,体重控制量
        for(int i =0; i < keyArray.length; i++){
            if("bs".equals(keyArray[i])){
                continue;
            }
            IndicatorBean userDataBaseBean = new IndicatorBean();
            BodyFatWeighIndicatorInfo weightIndicatorInfo = fatWeightIndicatorInfoMap.get(keyArray[i]);
            if(weightIndicatorInfo!=null){
                userDataBaseBean.setIndicatorName(weightIndicatorInfo.getIndicatorName());
                userDataBaseBean.setIndicatorValue(weightIndicatorInfo.getIndicatorValue());
                userDataBaseBean.setIndicatorUnit(weightIndicatorInfo.getIndicatorUnit());
                userDataBaseBean.setIndicatorDesc(weightIndicatorInfo.getIndicatorDesc());
                userDataBaseBean.setIndicatorNote(weightIndicatorInfo.getIndicatorNote());

                List<IndicatorResultRangeInfo> indicatorResultRange = weightIndicatorInfo.getIndicatorResultRange();
                userDataBaseBean.setIndicatorResultRanges(indicatorResultRange);
                String indicatorResult = weightIndicatorInfo.getIndicatorResult();
                if (TextUtils.isEmpty(indicatorResult)) {
                    indicatorResult = "";
                } else if (FatScalUtil.INDICATORRESULT_MIN_VALUE.equals(indicatorResult)) {
                    indicatorResult = indicatorResultRange.get(0).getRangeValue();
                    userDataBaseBean.setMinValue(true);
                } else if (FatScalUtil.INDICATORRESULT_MAX_VALUE.equals(indicatorResult)) {
                    indicatorResult = indicatorResultRange.get(indicatorResultRange.size() - 1).getRangeValue();
                    userDataBaseBean.setMaxValue(true);
                }
                userDataBaseBean.setIndicatorResult(indicatorResult);

                HashMap<String, String> map = new HashMap();
                if(indicatorResultRange!= null){
                    for (IndicatorResultRangeInfo indicatorResultRangeInfo : indicatorResultRange) {
                        if (indicatorResultRangeInfo != null) {
                            String rangeName = indicatorResultRangeInfo.getRangeName();
                            String rangeValue = indicatorResultRangeInfo.getRangeValue();
                            map.put(rangeValue, rangeName);
                        }
                    }
                    String value = map.get(indicatorResult);
                    userDataBaseBean.setResultRangeMap(map);
                    userDataBaseBean.setLevelValue(value);

                    //获取中间指标区间范围索引
                    List<Integer> indexList = findStandardLevel(indicatorResultRange);

                    //标准区间
                    ArrayList<Segment> standardSegments = new ArrayList<>();
                    for(int k = 0;k<indexList.size();k++){//标准区间
                        Segment segment = new Segment();
                        segment.setDescriptionText(indicatorResultRange.get(indexList.get(k)).getRangeName());
                        segment.setColor(FatScalUtil.SEGMENT_BG_COLORS[FatScalUtil.STANDARD_COLOR_INDEX]);
                        standardSegments.add(segment);
                    }

                    int minStandardIndex = -1;
                    int maxStandardIndex = -1;
                    if(!indexList.isEmpty()){
                        minStandardIndex = indexList.get(0);
                        maxStandardIndex = indexList.get(indexList.size()-1);
                    }
                    int indicatorLevel = -1;
                    try {
                        int indicatorResultIndex = Integer.valueOf(indicatorResult);
                        if(indicatorResultIndex > maxStandardIndex){
                            indicatorLevel = FatScalUtil.HIGH_LEVEL_VALUE;
                        } else if(indicatorResultIndex < minStandardIndex){
                            indicatorLevel = FatScalUtil.LOW_LEVEL_VALUE;
                        } else if(indicatorResultIndex >= minStandardIndex && minStandardIndex <= maxStandardIndex){
                            indicatorLevel = FatScalUtil.NORMAL_LEVEL_VALUE;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    userDataBaseBean.setIndicatorLevel(indicatorLevel);
                    //当前指标值对应的图标
                    userDataBaseBean.setBodyIcon(FatScalUtil.getBodyIconResourceId(indicatorLevel));
                    if(minStandardIndex !=-1 && maxStandardIndex !=-1){
                        //标准左侧
                        ArrayList<Segment> lowSegments = new ArrayList<>();
                        for(int n = 0; n<minStandardIndex; n++){
                            Segment segment = new Segment();
                            if(n< FatScalUtil.SEGMENT_BG_COLORS.length){
                                segment.setColor(FatScalUtil.SEGMENT_BG_COLORS[FatScalUtil.STANDARD_COLOR_INDEX - (1+n)]);
                            } else{
                                segment.setColor(FatScalUtil.SEGMENT_BG_COLORS[0]);
                            }

                            segment.setDescriptionText(indicatorResultRange.get(n).getRangeName());
                            lowSegments.add(segment);
                        }

                        //标准右侧
                        ArrayList<Segment> highSegments = new ArrayList<>();
                        for(int p = 1; p< indicatorResultRange.size() - maxStandardIndex; p++){
                            Segment segment = new Segment();
                            if(p< FatScalUtil.SEGMENT_BG_COLORS.length - FatScalUtil.STANDARD_COLOR_INDEX){ // 获取右侧在颜色数组范围内的分段
                                segment.setColor(FatScalUtil.SEGMENT_BG_COLORS[FatScalUtil.STANDARD_COLOR_INDEX + p]);
                            } else {// 获取右侧在颜色数组范围外的分段
                                segment.setColor(FatScalUtil.SEGMENT_BG_COLORS[FatScalUtil.SEGMENT_BG_COLORS.length -1]);
                            }
                            segment.setDescriptionText(indicatorResultRange.get(p + maxStandardIndex).getRangeName());
                            highSegments.add(segment);
                        }
                        userDataBaseBean.getSegments().addAll(lowSegments);
                        userDataBaseBean.getSegments().addAll(standardSegments);
                        userDataBaseBean.getSegments().addAll(highSegments);

                        //分割点颜色
                        ArrayList<Integer> segmentPointsColors = new ArrayList<>();
                        for(int q = 0;q< userDataBaseBean.getSegments().size()-1;q++){
                            if(q< minStandardIndex){
                                segmentPointsColors.add(userDataBaseBean.getSegments().get(q).getColor());
                            } else if(q>=minStandardIndex && q<maxStandardIndex){
                                segmentPointsColors.add(userDataBaseBean.getSegments().get(q).getColor());
                            } else {
                                segmentPointsColors.add(userDataBaseBean.getSegments().get(q+1).getColor());
                            }
                        }
                        userDataBaseBean.setSegmentPointsColors(segmentPointsColors);
                    }
                }
                //设置分段区间最值
                try {
                    List<String> indicatorValueRange = weightIndicatorInfo.getIndicatorValueRange();
                    if(indicatorValueRange!=null && !indicatorValueRange.isEmpty()){
                        for(int t =0; t <userDataBaseBean.getSegments().size(); t++){
                            Segment segment = userDataBaseBean.getSegments().get(t);
                            segment.setMinValue(Float.valueOf(indicatorValueRange.get(t)));
                            segment.setMaxValue(Float.valueOf(indicatorValueRange.get(t+1)));
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
            if(!TextUtils.isEmpty(userDataBaseBean.getIndicatorValue())){
                segmentedDataList.add(userDataBaseBean);
            }
        }
        List<IndicatorBean> unNormalLevelDataList = new ArrayList<>();
        List<IndicatorBean> normalLevelDataList = new ArrayList<>();
        if (segmentedDataList != null && !segmentedDataList.isEmpty()) { //分类
            for (IndicatorBean bean : segmentedDataList) {
                if (bean.getIndicatorResultRanges() == null && FatScalUtil.STANDARD_KEYS.contains(bean.getIndicatorValue())) {
                    normalLevelDataList.add(bean);
                } else if (bean.getIndicatorLevel() == FatScalUtil.LOW_LEVEL_VALUE ||
                        bean.getIndicatorLevel() == FatScalUtil.HIGH_LEVEL_VALUE) {
                    unNormalLevelDataList.add(bean);
                } else if (bean.getIndicatorLevel() == FatScalUtil.NORMAL_LEVEL_VALUE) {
                    normalLevelDataList.add(bean);
                }
            }
        }
        segmentedDataList.clear();
        segmentedDataList.addAll(unNormalLevelDataList);
        segmentedDataList.addAll(normalLevelDataList);

        //设置指标分类开始title数据
        boolean hasFindUnHealth = false;
        boolean hasFindHealth = false;
        for (int i = 0; i < segmentedDataList.size(); i++) {
            if (hasFindUnHealth && hasFindHealth) {//健康,非健康指标都已找到
                break;
            }
            int indicatorLevel = segmentedDataList.get(i).getIndicatorLevel();
            if (!hasFindUnHealth && (indicatorLevel == FatScalUtil.LOW_LEVEL_VALUE ||
                    indicatorLevel == FatScalUtil.HIGH_LEVEL_VALUE)) {//非健康指标
                segmentedDataList.get(i).setClassifyBegain(true);
                segmentedDataList.get(i).setClassifyCount(unNormalLevelDataList.size());
                hasFindUnHealth = true;
            }
            if (!hasFindHealth && (indicatorLevel == FatScalUtil.NORMAL_LEVEL_VALUE)) {//健康指标
                segmentedDataList.get(i).setClassifyBegain(true);
                segmentedDataList.get(i).setClassifyCount(normalLevelDataList.size());
                hasFindHealth = true;
            }
        }
        return segmentedDataList;
    }

    //获取标准level 索引
    private List<Integer> findStandardLevel(List<IndicatorResultRangeInfo> rangeInfoList) {
        List<Integer> standardIndexs = new ArrayList<>();
        for(int i = 0;i<rangeInfoList.size();i++){
            if(FatScalUtil.STANDARD_KEYS.contains(rangeInfoList.get(i).getRangeName())){
                standardIndexs.add(i);
            }
        }
        return standardIndexs;
    }
}
