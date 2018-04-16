package com.cloudk;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义折线图
 */
public class LineChartView extends View {

    /* xy轴距离周边的距离，用于画刻度文本 */
    private int xyMargin = 100;
    /* 刻度线长度 */
    private int graduatedLineLength = 10;
    /* 坐标轴、刻度线的颜色 */
    private int mTextColor;
    /* 连接线的颜色 */
    private int mLineColor;
    /* 坐标点的颜色 */
    private int mPointColor;
    /* 坐标点的半径 */
    private int mPointRadius;

    private Paint mTextPaint;
    private Path mXyPath;
    private Paint mLinePaint;
    private Paint mPointPaint;
    private Paint mPointCirclePaint;

    private List<String> dateList;


    /* 坐标点的集合 */
    private List<ChartPoint> mPointList;
    /* 上次储存坐标点的集合，用于动画 */
    private List<ChartPoint> mLastPointList;
    private int maxX = 30;//xList的最大值
    private int maxY = 70;//yList的最大值

//    private OnPointClickListener mOnPointClickListener;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineChartView, defStyleAttr, 0);
        mTextColor = ta.getColor(R.styleable.LineChartView_textColor, 0xff381a59);
        mLineColor = ta.getColor(R.styleable.LineChartView_lineColor, 0xff8e29fa);
        mPointColor = ta.getColor(R.styleable.LineChartView_pointColor, 0xffff5100);
        mPointRadius = DensityUtils.dp2px(context, 1);
        ta.recycle();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(30);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(DensityUtils.dp2px(context, 2));
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mXyPath = new Path();

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setColor(mPointColor);
        mPointCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointCirclePaint.setStyle(Paint.Style.STROKE);
        mPointCirclePaint.setStrokeWidth(DensityUtils.dp2px(context, 2));
        mPointCirclePaint.setColor(mLineColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
    }

    private int measuredDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 500;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPointList == null || mPointList.size() == 0) {
            return;
        }

        mXyPath.reset();
        mXyPath.moveTo(xyMargin, 0);
        mXyPath.lineTo(xyMargin, getHeight() - xyMargin);
        mXyPath.lineTo(getWidth(), getHeight() - xyMargin);
        canvas.drawPath(mXyPath, mLinePaint);//画x、y坐标轴

        for (int i = 0; i < mPointList.size(); i++) {

            if(mPointList.get(i).getxData()%10==0)
            {
                //画x轴刻度线
                int x = xyMargin + (i) * (getWidth() - xyMargin) / mPointList.size();
                canvas.drawLine(x, getHeight() - xyMargin - graduatedLineLength, x, getHeight() - xyMargin, mLinePaint);
                //画坐标轴刻度文本
                canvas.drawText(dateList.get(i), x, getHeight() - mTextPaint.getTextSize() , mTextPaint);

            }
            //画y轴刻度线
            if(i%3==0)
            {
                int y = getHeight() - xyMargin - (i + 1) * (getHeight() - xyMargin) / mPointList.size();
                canvas.drawLine(xyMargin, y, xyMargin + graduatedLineLength, y, mLinePaint);
                canvas.drawText(String.valueOf((i + 1) * 10), 0, y + mTextPaint.getTextSize(), mTextPaint);
            }

        }

        for (int i = 0; i < mPointList.size(); i++) {
            int x = xyMargin + (i) * (getWidth() - xyMargin) / mPointList.size();
            int nextx = xyMargin + (i + 1) * (getWidth() - xyMargin) / mPointList.size();
            //画坐标点
            if (i != mPointList.size() - 1) {
                ChartPoint lastP = mPointList.get(i);
                ChartPoint nextP = mPointList.get(i + 1);
                canvas.drawLine(x, lastP.getY(), nextx, nextP.getY(), mLinePaint);
            }
            //画连接线
            ChartPoint point = mPointList.get(i);
            canvas.drawCircle(x, point.getY(), mPointRadius, mPointPaint);
            canvas.drawCircle(x, point.getY(), mPointRadius, mPointCirclePaint);
        }


    }

    /**
     * 设置数据
     *
     * @param xList x轴数据集合
     * @param yList y轴数据集合
     */
    public void setDataList(List<Integer> xList, List<Integer> yList, List<String> dateList) {
        this.dateList = dateList;
        if (xList == null || yList == null || xList.size() == 0 || yList.size() == 0) {
            throw new IllegalArgumentException("没有数据");
        }
        if (xList.size() != yList.size()) {
            throw new IllegalArgumentException("x、y轴数据长度不一致");
        }

        setPointData(xList, yList);
        setPointAnimator();
    }

    /**
     * 设置坐标点的数据、坐标
     *
     * @param xList x轴数据集合
     * @param yList y轴数据集合
     */
    private void setPointData(List<Integer> xList, List<Integer> yList) {
        mPointList = new ArrayList<>();
        for (int i = 0; i < xList.size(); i++) {
            ChartPoint point = new ChartPoint();
            //设置坐标点的xy数据
            point.setxData(xList.get(i));
            point.setyData(yList.get(i));
            point.setX(xyMargin + xList.get(i)* (getWidth() - xyMargin)/xList.size());
            point.setY(getHeight() - xyMargin - (getHeight() - xyMargin) * yList.get(i) / ((yList.size()/7)*maxY));
            mPointList.add(point);
        }
    }

    /**
     * 设置坐标点的动画
     */
    private void setPointAnimator() {
        for (int i = 0; i < mPointList.size(); i++) {
            final ChartPoint point = mPointList.get(i);
            ValueAnimator anim;
            anim = ValueAnimator.ofInt(getHeight() - xyMargin, point.getY());
            anim.setDuration(500);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    point.setY(value);
                    invalidate();
                }
            });
            anim.start();
        }
        //储存坐标点集合
//        mLastPointList = mPointList;
    }

}
