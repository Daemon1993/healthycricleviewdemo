package com.daemon1993.dhealthyprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;



/**
 *@author Daemon
 * create at 2016-04-29 17:30
 */
public class DHealthyProgressView extends RelativeLayout {

    private static final String TAG = DHealthyProgressView.class.getName();


    private Interpolator interpolator = new AccelerateDecelerateInterpolator();

    private int dBaserPainterColor = Color.YELLOW;
    private int progressColor = Color.WHITE;
    private float min = 0;
    private float begin = min;
    private float max = 100;
    private ValueAnimator valueAnimator;
    private OnValueChangeListener valueChangeListener;
    private float mValue;
    private int duration = 1000;

    private int progressStrokeWidth = 48;
    private int paddingT;
    private boolean isComplete;

    private int dashWith = 5;
    private int dashSpace = 8;


    private Paint progressPaint;
    private int outPadding;
    private Paint basePaint;
    private RectF baseCircle;
    private RectF progressCircle;
    private float baseStartAngle = 270f;
    private float baseFinishAngle = 359.8f;
    private float progressStartAngle = 270f;
    private float plusAngle;
    private int paddingR;
    private int paddingL;
    private int paddingB;
    private boolean isContinue;


    public DHealthyProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public DHealthyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        setWillNotDraw(false);
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.DashedCircularProgress, defStyleAttr, 0);
        //获取padding属性

        paddingT = getPaddingTop();
        paddingL = getPaddingLeft();
        paddingR = getPaddingRight();
        paddingB = getPaddingBottom();


        paddingT = paddingT + progressStrokeWidth / 2;
        paddingL = paddingL + progressStrokeWidth / 2;
        paddingR = paddingR + progressStrokeWidth / 2;
        paddingB = paddingB + progressStrokeWidth / 2;

        Log.e(TAG, "padding " + paddingT);

        initAttributes(attributes);




    }

    private void initAttributes(TypedArray attributes) {

        dBaserPainterColor = attributes.getColor(R.styleable.DashedCircularProgress_base_color,
                dBaserPainterColor);
        progressColor = attributes.getColor(R.styleable.DashedCircularProgress_progress_color,
                progressColor);
        max = attributes.getFloat(R.styleable.DashedCircularProgress_max, max);
        duration = attributes.getInt(R.styleable.DashedCircularProgress_duration, duration);

        progressStrokeWidth = attributes.getDimensionPixelOffset(R.styleable.DashedCircularProgress_progress_stroke_width,
                progressStrokeWidth);
        initPainters();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onlayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.e(TAG, "initRectF " + w + "<--->" + h);

        initBaseRectF(h, w);
        initProgressRectF(h, w);

    }

    private void initProgressRectF(int h, int w) {
        progressCircle = new RectF();
        progressCircle.set(paddingL, paddingT, w - paddingR, h - paddingB);
    }

    private void initBaseRectF(int h, int w) {
        baseCircle = new RectF();
        baseCircle.set(paddingL, paddingT, w - paddingR, h - paddingB);
    }

    private void initPainters() {



        initBasePainter();

        initProgressPainter();

        initValueAnimator();


    }

    /**
     * 初始化进度画笔
     */
    private void initProgressPainter() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(progressStrokeWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace},
                dashSpace));

    }

    /**
     * 初始化 底层 画笔
     */
    private void initBasePainter() {
        basePaint = new Paint();
        basePaint.setAntiAlias(true);
        basePaint.setStrokeWidth(progressStrokeWidth);
        basePaint.setColor(dBaserPainterColor);
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace},
                dashSpace));

    }


    /**
     * 进度显示动画效果初始化
     */
    private void initValueAnimator() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimatorListenerImp());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.e(TAG, "onDraw");


        canvas.drawArc(baseCircle, baseStartAngle, baseFinishAngle, false, basePaint);

        canvas.drawArc(progressCircle, progressStartAngle, plusAngle, false, progressPaint);

        if (isComplete) {
            return;
        }
        invalidate();

    }

    /**
     * 设置进度值 重新开始绘制
     *
     * @param mValue
     */
    public void setmValue(float mValue) {

        if(this.mValue>=max){
            Log.e(TAG,"最大值超出");
            return;
        }

        if(isContinue){
            this.mValue+=mValue;
        }else {
            begin=min;
            this.mValue = mValue;
        }


        //最大值 最小值的限制
        this.mValue=this.mValue>max?max:this.mValue;

        this.mValue=this.mValue<min?min:this.mValue;

        isComplete = false;
        invalidate();
        if (mValue <= max || mValue >= min) {
            animateValue();
        }
    }

    /**
     * 属性动画开始
     */
    private void animateValue() {
        if (valueAnimator != null) {
            valueAnimator.setFloatValues(begin, mValue);
            valueAnimator.setDuration(duration);
            valueAnimator.start();

        }
    }

    public void setOnValueChangeListener(OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        if (valueAnimator != null) {
            valueAnimator.setInterpolator(interpolator);
        }
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;

    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    /**
     * 开启连续模式 每次在原有的进度上增加进度
     * @param b
     */
    public void beginContinue(boolean b) {
        isContinue=b;
    }

    /**
     * 动画开始 更新进度显示
     */
    private class ValueAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();

            plusAngle = (baseFinishAngle * value) / max;

            if (valueChangeListener != null) {
                valueChangeListener.onValueChange(value);
            }

            begin = value;

            if (begin == mValue) {
                isComplete = true;
            }
        }


    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    public void reset() {
        begin = min;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getdBaserPainterColor() {
        return dBaserPainterColor;
    }

    public void setdBaserPainterColor(int dBaserPainterColor) {
        this.dBaserPainterColor = dBaserPainterColor;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }
}