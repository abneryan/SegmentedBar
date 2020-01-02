package com.abneryan.segmentedbar.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.abneryan.segmentedbar.R;
import com.abneryan.segmentedbar.bean.IndicatorBean;
import com.abneryan.segmentedbar.bean.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SegmentedBarView extends View {

    private RectF roundRectangleBounds;
    private Paint fillPaint;
    private Paint signPaint;
    private Float indicatorValue;                                        //滑块显示的数值
    private Rect rectBounds;
    private float valueSignCenter = -1;                         //索引icon位置X坐标
    private int barHeight;                                       //分段条的高度

    private List<Segment> segments;                             //分段控件集合
    private int barRoundingRadius = 0;

    private int descriptionTextColor = Color.DKGRAY;             //描述文字字体颜色，默认灰色
    private int descriptionTextSize;                             //描述文字字体大小
    private int descriptionHeight;                            //底部描述文本所占的高度
    private int descriptionPaddingTop;
    private int bodyIconHeight;
    private int segmentMostNum;
    private Paint descriptionTextPaint;

    private List<Integer> segmentPointsColors = new ArrayList<>(); //分割点颜色

    private Rect segmentRect;

    private Bitmap mBitmap;
    private int thembH, thembW;
    private Paint inPointPaint;
    private Paint outPointPaint;
    private Resources resources;
    private int pointCircleRadius;
    private int pointCircleBorder;
    private Map<String, String> resultRangeMap;
    private String indicatorResult;
    private IndicatorBean indicatorBean;

    public SegmentedBarView(Context context) {
        super(context);
        init(context, null);
    }

    public SegmentedBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SegmentedBarView, 0, 0);
        try {
            resources = getResources();
            descriptionTextSize = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_description_text_size, resources.getDimensionPixelSize(R.dimen.sbv_description_text_size));
            barHeight = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_bar_height, resources.getDimensionPixelSize(R.dimen.sbv_bar_height));
            pointCircleRadius = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_point_circle_radius, resources.getDimensionPixelSize(R.dimen.sbv_point_circle_radius));
            barRoundingRadius = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_bar_rounding_radius_size, resources.getDimensionPixelSize(R.dimen.sbv_bar_rounding_radius_size));
            pointCircleBorder = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_point_circle_border_size, resources.getDimensionPixelSize(R.dimen.sbv_point_circle_border_size));
            descriptionHeight = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_description_height, resources.getDimensionPixelSize(R.dimen.sbv_description_height));
            descriptionPaddingTop = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_description_paddingtop, resources.getDimensionPixelSize(R.dimen.sbv_description_paddingtop));
            bodyIconHeight = a.getDimensionPixelSize(R.styleable.SegmentedBarView_sbv_body_icon_height, resources.getDimensionPixelSize(R.dimen.sbv_body_icon_height));
            descriptionTextColor = a.getColor(R.styleable.SegmentedBarView_sbv_description_text_color, descriptionTextColor);
            segmentMostNum = a.getInteger(R.styleable.SegmentedBarView_sbv_segment_most_number,  resources.getInteger(R.integer.sbv_segment_most_number));
        } finally {
            a.recycle();
        }

        descriptionTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        descriptionTextPaint.setColor(Color.DKGRAY);
        descriptionTextPaint.setStyle(Paint.Style.FILL);
        descriptionTextPaint.setTextAlign(Paint.Align.CENTER);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        signPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        signPaint.setStyle(Paint.Style.FILL);
        signPaint.setAntiAlias(true);

        outPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outPointPaint.setStyle(Paint.Style.FILL);
        outPointPaint.setAntiAlias(true);

        inPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inPointPaint.setStyle(Paint.Style.FILL);
        inPointPaint.setColor(Color.WHITE);
        inPointPaint.setAntiAlias(true);

        rectBounds = new Rect();
        roundRectangleBounds = new RectF();
        segmentRect = new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        valueSignCenter = -1;

        int segmentsSize = segments == null ? 0 : segments.size();
        if (segmentsSize > 0) {
            for (int segmentIndex = 0; segmentIndex < segmentsSize; segmentIndex++) {//画分段控件
                Segment segment = segments.get(segmentIndex);
                drawSegment(canvas, segment, segmentIndex, segmentsSize);
            }
        }

        if (mBitmap != null) {
            drawSliderImg(canvas, mBitmap, valueSignCenter, rectBounds.top + barHeight/2, signPaint);
        }
    }

    /**
     * 自定义分段thumb 图片指示---如果想自定义标记，可以继承SegmentedBarView，重写drawSliderImg方法
     *
     * @param canvas  画板
     * @param bitmap  图片对应的bitmap
     * @param centerX 当前进度所在的中心点x坐标
     * @param centerY 当前进度所在的中心点y坐标
     * @param paint   画笔paint
     */
    private void drawSliderImg(Canvas canvas, Bitmap bitmap, float centerX, float centerY, Paint paint) {
        canvas.drawBitmap(bitmap, centerX - thembW / 2, centerY - thembH, paint);
    }

    private int getContentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private void drawSegment(Canvas canvas, Segment segment, int segmentIndex, int segmentsSize) {
        boolean isLeftSegment = segmentIndex == 0;
        boolean isRightSegment = segmentIndex == segmentsSize - 1;
        boolean isLeftAndRight = isLeftSegment && isRightSegment;
        float singleSegmentWidth, segmentLeft, segmentRight;

        singleSegmentWidth = getContentWidth() / segmentsSize;
        segmentLeft = singleSegmentWidth * segmentIndex;
        segmentRight = segmentLeft + singleSegmentWidth;
        // Segment bounds
        rectBounds.set((int) segmentLeft + getPaddingLeft(), bodyIconHeight +
                getPaddingTop(), (int) segmentRight + getPaddingLeft(),  barHeight + bodyIconHeight + getPaddingTop());
       if(segment.getDescriptionText().equals(resultRangeMap.get(indicatorResult))){ //获取valueSign icon X 坐标
           if(indicatorBean.isMinValue()){
               valueSignCenter = getPaddingLeft();
           }else if(indicatorBean.isMaxValue()){
               valueSignCenter = getContentWidth()+ getPaddingLeft();
           }else if(indicatorValue == null || segment.getMinValue() == -1 || segment.getMaxValue() == -1){
               float valueSignCenterPercent = 0.5f;
               valueSignCenter = (segmentLeft + getPaddingLeft() + valueSignCenterPercent * singleSegmentWidth);
           }else if (indicatorValue != null && (indicatorValue >= segment.getMinValue() && indicatorValue < segment.getMaxValue() || (isRightSegment && segment.getMaxValue() == indicatorValue))) {
               float valueSignCenterPercent = (indicatorValue - segment.getMinValue()) / (segment.getMaxValue() - segment.getMinValue());
               valueSignCenter = (segmentLeft + getPaddingLeft() + valueSignCenterPercent * singleSegmentWidth);
               if(!isLeftAndRight && segmentIndex !=0){//当valueSign 落在分割线之上时,且当前不在最左侧分段上,分度数目大于1
                   if(indicatorValue == segment.getMinValue()){
                       valueSignCenter += pointCircleRadius;
                   }
               }
           }
       }

        fillPaint.setColor(resources.getColor(segment.getColor()));
        segmentRect.set(rectBounds);
        // Drawing segment (with specific bounds if left or right)
        if (isLeftSegment || isRightSegment) {
            roundRectangleBounds.set(rectBounds.left, rectBounds.top, rectBounds.right, rectBounds.bottom);
            canvas.drawRoundRect(roundRectangleBounds, barRoundingRadius, barRoundingRadius, fillPaint);
            if (!isLeftAndRight) {
                if (isLeftSegment) {
                    rectBounds.set((int) segmentLeft + barRoundingRadius + getPaddingLeft(),
                            bodyIconHeight + getPaddingTop(), (int)
                                    segmentRight + getPaddingLeft(), barHeight +
                                    bodyIconHeight + getPaddingTop());
                    canvas.drawRect(rectBounds, fillPaint);
                } else {
                    rectBounds.set((int) segmentLeft + getPaddingLeft(),
                            bodyIconHeight + getPaddingTop(), (int) segmentRight -
                                    barRoundingRadius + getPaddingRight(), barHeight +
                                    bodyIconHeight + getPaddingTop());
                    canvas.drawRect(
                            rectBounds,
                            fillPaint
                    );
                }
            }
        } else {
            canvas.drawRect(rectBounds, fillPaint);
        }
        if (segmentIndex !=0) {
            drawSegmentPoint(canvas, pointCircleRadius, rectBounds.left, rectBounds.top + barHeight / 2, segmentPointsColors.get(segmentIndex-1), outPointPaint, inPointPaint);
        }
        //设置字体大小之后才可以测量到文本的实际宽度
        descriptionTextPaint.setTextSize(descriptionTextSize);
        //获取字体的长度
        String descriptionText = segment.getDescriptionText();
        float fontWidth = descriptionTextPaint.measureText(descriptionText);
        if(fontWidth >= singleSegmentWidth){ //当文本长度大于分段长度时进行等比缩小
            descriptionTextSize = (int)(descriptionTextSize * singleSegmentWidth/fontWidth);
        }
        //Drawing segment description text
        descriptionTextPaint.setTextSize(descriptionTextSize);
        descriptionTextPaint.setColor(descriptionTextColor);
        drawTextCentredInRectWithSides(canvas, descriptionTextPaint, descriptionText,
                segmentRect.left, segmentRect.top, segmentRect.right,
                segmentRect.bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = getPaddingLeft() + getPaddingRight();
        int height = barHeight;
        int minHeight = height + getPaddingBottom() + getPaddingTop();
        minHeight += descriptionHeight + bodyIconHeight + descriptionPaddingTop;

        int w = resolveSizeAndState(minWidth, widthMeasureSpec, 0);
        int h = resolveSizeAndState(minHeight, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }
    private void drawTextCentredInRectWithSides(Canvas canvas, Paint paint, String text, float left, float top, float
            right, float bottom) {
        float textHeight = paint.descent() - paint.ascent(); //ascent：是baseline之上至字符最高处的距离(值为负),descent：是baseline之下至字符最低处的距离(值为正)
        if (TextUtils.isEmpty(text)) return;
        canvas.drawText(text, (left + right) / 2, bottom + textHeight + descriptionPaddingTop, paint);
    }

    /**
     * 获取指定长度的数据list ，彩线分段数目上限
     * @param segments
     */
    private void setSegments(List<Segment> segments) {
        if(segments.size()<= segmentMostNum){ //总数据长度小于目标长度
            this.segments = segments;
        } else{//总数据长度大于，等于目标长度
            int indictorIndex = -1;
            for(int i = 0;i<segments.size();i++){ //获取指标当前值所对应的索引
                if(resultRangeMap.get(indicatorResult).equals(segments.get(i).getDescriptionText())){
                    indictorIndex = i;
                    break;
                }
            }
            if (indictorIndex < segmentMostNum) {//当前位置在规定分段数目的左侧
                this.segments = segments.subList(0, segmentMostNum);
            } else {//当前位置在规定分段数目的右侧
                this.segments = segments.subList(indictorIndex - segmentMostNum + 1, indictorIndex + 1);
            }
        }
    }
    private void setSegmentPointColors(ArrayList<Integer> segmentPointsColors) {
        if (indicatorBean.getSegments().size() <= segmentMostNum) { //总数据长度小于目标长度
            this.segmentPointsColors = segmentPointsColors;
        } else {//总数据长度大于，等于目标长度
            int indictorIndex = -1;
            for (int i = 0; i < indicatorBean.getSegments().size(); i++) { //获取指标当前值所对应的索引
                if (resultRangeMap.get(indicatorResult).equals(indicatorBean.getSegments().get(i).getDescriptionText())) {
                    indictorIndex = i;
                    break;
                }
            }
            if (indictorIndex < segmentMostNum) {//当前位置在规定分段数目的左侧
                this.segmentPointsColors = segmentPointsColors.subList(0, segmentMostNum - 1);
            } else {//当前位置在规定分段数目的右侧
                this.segmentPointsColors = segmentPointsColors.subList(indictorIndex - segmentMostNum + 1, indictorIndex);
            }

        }
    }

    private void setResultRangeMap(Map<String, String> resultRangeMap) {
        this.resultRangeMap = resultRangeMap;
    }

    private void setIndicatorResult(String indicatorResult) {
        this.indicatorResult = indicatorResult;
    }

    private void setIndicatorValue(String indicatorValue) {
        try {
            this.indicatorValue = Float.valueOf(indicatorValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private void setSliderImage(int sliderImage){
        if (sliderImage != -1) {
            mBitmap = BitmapFactory.decodeResource(getResources(), sliderImage);
            thembH = mBitmap.getHeight();
            thembW = mBitmap.getWidth();
        }
    }


    protected void drawSegmentPoint(Canvas canvas, int pointSize, float centerX, float centerY, int color, Paint outPointPaint, Paint inPointPaint) {
        outPointPaint.setColor(resources.getColor(color));
        canvas.drawCircle(centerX, centerY, pointSize, outPointPaint);
        canvas.drawCircle(centerX, centerY, Math.abs(pointSize - pointCircleBorder), inPointPaint);
    }
    public void setDatas(IndicatorBean bean) {
        if(bean == null){
            return;
        }
        indicatorBean = bean;
        setIndicatorValue(bean.getIndicatorValue());
        setSliderImage(bean.getBodyIcon());
        setSegmentPointColors(bean.getSegmentPointsColors());
        setResultRangeMap(bean.getResultRangeMap());
        setIndicatorResult(bean.getIndicatorResult());
        setSegments(bean.getSegments());
        invalidate();
        requestLayout();
    }
}
