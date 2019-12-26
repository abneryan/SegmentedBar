package com.abneryan.segmentedbar;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abneryan.segmentedbar.bean.IndicatorBean;
import com.abneryan.segmentedbar.utils.AnimationUtils;
import com.abneryan.segmentedbar.utils.FatScalUtil;
import com.abneryan.segmentedbar.view.SegmentedBarView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SegmentedtAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final TimeInterpolator mInterpolator = AnimationUtils.createInterpolator(AnimationUtils.ACCELERATE_DECELERATE_INTERPOLATOR);;
    private List<IndicatorBean> mSegmentedList = new ArrayList<>();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private ItemExpandStateListener itemExpandStateListener;

    public SegmentedtAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.segment_recycler_view_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(mSegmentedList == null || mSegmentedList.size()==0){
            return;
        }
        final IndicatorBean bean = mSegmentedList.get(position);

            final ViewHolder viewHolder = (ViewHolder)holder;
            String indicatorUnit = null;
            String indicatorName = null;
            String indicatorValue= null;
            String indicatorNote = null;
            String levelValue = null;
            HashMap<String, String> resultRangeMap = null;
            if(bean!= null) {
                indicatorUnit = bean.getIndicatorUnit();
                indicatorName = bean.getIndicatorName();
                indicatorValue = bean.getIndicatorValue();
                indicatorNote = bean.getIndicatorNote();
                levelValue = bean.getLevelValue();
                resultRangeMap = bean.getResultRangeMap();
                if (TextUtils.isEmpty(indicatorValue)) {
                    indicatorValue = "--";
                }
                if (TextUtils.isEmpty(indicatorName)) {
                    indicatorName = "--";
                }
                if(TextUtils.isEmpty(indicatorNote)){
                    indicatorNote = "";
                }
                if (TextUtils.isEmpty(indicatorUnit)) {
                    indicatorUnit = "";
                } else {
                    indicatorUnit = "(" + indicatorUnit + ")";
                }

            }
            viewHolder.setIsRecyclable(false);
            viewHolder.mNameView.setText(indicatorName);
            final int indicatorLevel = bean.getIndicatorLevel();
            viewHolder.mNumlView.setText(indicatorValue + indicatorUnit);

            if(TextUtils.isEmpty(levelValue)){
                viewHolder.mLevelView.setVisibility(View.GONE);
            } else {
                viewHolder.mLevelView.setText(levelValue);
                viewHolder.mLevelView.setBackground(mContext.getResources().getDrawable(FatScalUtil.getLevelBgResource(indicatorLevel)));
                viewHolder.mLevelView.setVisibility(View.VISIBLE);
            }
            if(TextUtils.isEmpty(indicatorNote)){
                viewHolder.mDescView.setVisibility(View.GONE);
            } else {
                viewHolder.mDescView.setText(indicatorNote);
                viewHolder.mDescView.setVisibility(View.VISIBLE);
            }
            if(resultRangeMap!= null && resultRangeMap.size()>0){
                viewHolder.mBodyView.setVisibility(View.VISIBLE);
                viewHolder.mBodyView.setDatas(bean);
            } else{
                viewHolder.mBodyView.setVisibility(View.GONE);
            }
            //分类title
            viewHolder.mTitleView.setVisibility(View.GONE);
            if(indicatorLevel == FatScalUtil.LOW_LEVEL_VALUE || indicatorLevel == FatScalUtil.HIGH_LEVEL_VALUE){
                if(bean.isClassifyBegain()){
                    viewHolder.mTitleView.setVisibility(View.VISIBLE);
                    String tempStr = mContext.getResources().getString(R.string.indicator_warning_tip_title);
                    tempStr = String.format(tempStr,bean.getClassifyCount());
                    viewHolder.mTitleView.setText(tempStr);
                }
            } else if(indicatorLevel == FatScalUtil.NORMAL_LEVEL_VALUE){
                if(bean.isClassifyBegain()){
                    viewHolder.mTitleView.setVisibility(View.VISIBLE);
                    String tempStr = mContext.getResources().getString(R.string.indicator_standard_tip_title);
                    tempStr = String.format(tempStr,bean.getClassifyCount());
                    viewHolder.mTitleView.setText(tempStr);
                }
            }
            viewHolder.expandableLayout.setInterpolator((Interpolator) mInterpolator);
            viewHolder.expandableLayout.setExpanded(expandState.get(position), false);
            if((resultRangeMap == null ||(resultRangeMap!= null && resultRangeMap.size()==0))
                    && TextUtils.isEmpty(indicatorNote)){//resultRangeMap 为空或者resultRangeMap不为空且大小为0，并且同时indicatorNote为空
                viewHolder.itemView.setOnClickListener(null);
            }else{
                viewHolder.expandableLayout.setOnExpansionUpdateListener(new net.cachapa.expandablelayout.ExpandableLayout.OnExpansionUpdateListener() {
                    @Override
                    public void onExpansionUpdate(float expansionFraction, int state) {
                        if (state == ExpandableLayout.State.EXPANDED) {
                            if (itemExpandStateListener != null) {
                                itemExpandStateListener.scrollItem(position);
                            }
                        } else if (state == ExpandableLayout.State.COLLAPSED) {
                            if (itemExpandStateListener != null) {
                                itemExpandStateListener.onItemCollapsed();
                            }
                        }
                    }
                });
            }
            viewHolder.mArrowView.setRotation(expandState.get(position) ? 180f : 0f);
    }

    @Override
    public int getItemCount() {
        return mSegmentedList.size();
    }

    public void updateList(List<IndicatorBean> newDatas) {
        if (newDatas != null && newDatas.size()!=0) {
            mSegmentedList = newDatas;
        }
        for (int i = 0; i < mSegmentedList.size(); i++) {
            expandState.append(i, false);
        }
        notifyDataSetChanged();
    }

    public void setItemExpandStateListener(ItemExpandStateListener itemExpandStateListener) {
        this.itemExpandStateListener = itemExpandStateListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitleView;
        public TextView mNameView;
        public TextView mLevelView;
        public TextView mNumlView;
        public ImageView mArrowView;
        public SegmentedBarView mBodyView;
        public TextView mDescView;
        public net.cachapa.expandablelayout.ExpandableLayout expandableLayout;

        public ViewHolder(View view) {
            super(view);
            mTitleView = view.findViewById(R.id.report_title_tv);
            mNameView = view.findViewById(R.id.report_item_name_tv);
            mLevelView = view.findViewById(R.id.report_item_level_tv);
            mNumlView = view.findViewById(R.id.report_item_num_tv);
            mArrowView = view.findViewById(R.id.report_item_arrow_iv);
            mBodyView = view.findViewById(R.id.report_item_body_icon);
            mDescView = view.findViewById(R.id.report_item_desc_tv);
            expandableLayout = view.findViewById(R.id.report_item_expandableLayout);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ViewHolder viewHolder = (ViewHolder) v.getTag();
            if (viewHolder.expandableLayout.getState() == ExpandableLayout.State.COLLAPSED) {
                viewHolder.expandableLayout.expand();
                AnimationUtils.createRotateAnimator(viewHolder.mArrowView, 0f, 180f).start();
                expandState.put(position, true);
            } else if (viewHolder.expandableLayout.getState() == ExpandableLayout.State.EXPANDED) {
                viewHolder.expandableLayout.collapse();
                AnimationUtils.createRotateAnimator(viewHolder.mArrowView, 180f, 0f).start();
                expandState.put(position, false);
            }
        }
    }
    interface ItemExpandStateListener {
        void scrollItem(int position);

        void onItemCollapsed();
    }
}
