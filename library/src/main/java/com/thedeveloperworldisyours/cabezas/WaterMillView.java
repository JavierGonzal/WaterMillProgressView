package com.thedeveloperworldisyours.cabezas;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by javierg on 28/09/2016.
 */

public class WaterMillView extends View {

    private int mWidth;
    private int mHeight;

    /**
     * Wave speed
     */
    private static final int WAVE_SPEED = 70;

    private int mMaxProgress = 100;
    private int mCurrentProgress = 35;
    private float mCurY;

    private float mDistance = 0;
    private int mRefreshGap = 1;

    private static final int INVALIDATE = 0X777;


    /**
     * Default Diameter size
     */
    private static final int DEFAULT_DIAMETER_SIZE = 120;

    /**
     * Ratio line start X
     */
    private static final float RATIO_LINE_START_X = 5 / 6.f;

    /**
     * Ratio line start Y
     */
    private static final float RATIO_LINE_START_Y = 1 / 2.f;

    /**
     * Ratio arc start X
     */
    private static final float RATIO_ARC_START_X = 2 / 5.f;

    /**
     * Mill separation angle
     */
    private static final float Mill_SEPARATION_ANGLE = 45;

    /**
     * Water level
     */
    private static final int DEFAULT_WATER_LEVEL = 50;

    /**
     * Wave Color
     */
    private int mWaveColor;

    /**
     * Default wave Color
     */
    private int DEFAULT_WAVE_COLOR = Color.CYAN;

    /**
     * Mill Color
     */
    private int mMillColor;

    /**
     * Default mill Color
     */
    private int DEFAULT_MILL_COLOR = Color.DKGRAY;

    /**
     * Background Color
     */
    private int mBackgroundColor;

    /**
     * Default background Color
     */
    private int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    /**
     * Text Color
     */
    private int mTextColor;

    /**
     * Default text Color
     */
    private int DEFAULT_TEXT_COLOR = Color.BLACK;

    /**
     * space hourglass
     */
    private static final float SPACE_MILL = 12;

    /**
     * Mill line length
     */
    private static final float MILL_LINE_LENGTH = 15;

    /**
     * default offset Y
     */
    private static final int DEFAULT_OFFSET_Y = 70;

    /**
     * (mLineStartX, mLineStartY)，mLineLength
     */
    private float mLineStartX, mLineStartY, mLineLength;

    /**
     * x,y
     */
    private float textX, textY;

    /**
     * Radios
     */
    private float sunRadius;

    /**
     * x,y
     */
    private double millStartX, millStartY, millStopX, millStopY;

    /**
     * Wave height
     */
    private static final float WAVE_HEIGHT = 20f;

    /**
     * Wave half width
     */
    private static final float WAVE_HALF_WIDTH = 50f;

    private String mLoadingString;

    private float offsetY = DEFAULT_OFFSET_Y, mOffsetSpin;

    private Paint mPaint, mMillPaint, mPathPaint;
    private Path mPath;

    private TextPaint mTextPaint;

    private RectF rectF;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INVALIDATE:
                    invalidate();
                    sendEmptyMessageDelayed(INVALIDATE, mRefreshGap);
                    break;
            }
        }
    };

    public WaterMillView(Context context) {
        this(context, null);
    }

    public WaterMillView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterMillView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaterMillView, R.attr.waterMillViewStyle, 0);
        mWaveColor = attributes.getColor(R.styleable.WaterMillView_wave_color, DEFAULT_WAVE_COLOR);
        mTextColor = attributes.getColor(R.styleable.WaterMillView_text_color, DEFAULT_TEXT_COLOR);
        mMillColor = attributes.getColor(R.styleable.WaterMillView_mill_color, DEFAULT_MILL_COLOR);
        mBackgroundColor = attributes.getColor(R.styleable.WaterMillView_background_color, DEFAULT_BACKGROUND_COLOR);
        mLoadingString = attributes.getString(R.styleable.WaterMillView_loading_text);
        mCurrentProgress = attributes.getInteger(R.styleable.WaterMillView_water_level, DEFAULT_WATER_LEVEL);
        if (mLoadingString == null) {
            mLoadingString = getResources().getString(R.string.loading);
        }
        setBackgroundColor(mBackgroundColor);
        initRes();
    }

    private void initRes() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(mMillColor);

        mMillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMillPaint.setStyle(Paint.Style.STROKE);
        mMillPaint.setStrokeWidth(10);
        mMillPaint.setColor(mMillColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(35);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.FILL);
        mPathPaint.setStrokeWidth(10);

        mHandler.sendEmptyMessageDelayed(INVALIDATE, 100);

        rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize;
        int heightSize;

        Resources r = Resources.getSystem();
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DIAMETER_SIZE, r.getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            mWidth = widthSize + 400;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DIAMETER_SIZE, r.getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            mCurY = mHeight = heightSize;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int width = getWidth();
        final int height = getHeight();


        mLineLength = width * RATIO_LINE_START_X;

        mLineStartX = (width - mLineLength) * .5f;
        mLineStartY = height * RATIO_LINE_START_Y;

        textX = width * .5f;
        textY = mLineStartY + (height - mLineStartY) * .5f + Math.abs(mTextPaint.descent() + mTextPaint.ascent()) * .5f;


        sunRadius = (mLineLength - mLineLength * RATIO_ARC_START_X) * .5f;

        calculateAndSetRectPoint();
        initAnimationDriver();
    }


    /**
     * calculated and set rectangle point
     */
    private void calculateAndSetRectPoint() {
        float rectLeft = mLineStartX + mLineLength * .5f - sunRadius;
        float rectTop = mLineStartY - sunRadius + offsetY;
        float rectRight = mLineLength - rectLeft + 2 * mLineStartX;
        float rectBottom = rectTop + 2 * sunRadius;

        rectF.set(rectLeft, rectTop, rectRight, rectBottom);
    }

    /**
     * Calculated center
     */
    public float calculateCenter() {
        float rectLeft = mLineStartX + mLineLength * .5f - sunRadius;
        float rectRight = mLineLength - rectLeft + 2 * mLineStartX;
        return rectRight - rectLeft;
    }

    /**
     * init animation driver
     */
    private void initAnimationDriver() {

        startSpinAnimation();

    }

    /**
     * Started spin animation
     */
    private void startSpinAnimation() {
        ValueAnimator spinAnima = ValueAnimator.ofFloat(0, 360);
        spinAnima.setRepeatCount(-1);
        spinAnima.setDuration(24 * 1000);
        spinAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffsetSpin = Float.parseFloat(animation.getAnimatedValue().toString());
                postInvalidate();
            }
        });
        spinAnima.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(calculateCenter(), mLineStartY, sunRadius, mPaint);
        canvas.drawCircle(calculateCenter(), mLineStartY, sunRadius / 2, mPaint);

        drawRadiusCenter(canvas);
        drawShovel(canvas);

        drawWave(canvas);
        canvas.drawText(mLoadingString, textX, textY, mTextPaint);
    }

    /**
     * Drawn Shovel
     *
     * @param canvas
     */
    private void drawShovel(Canvas canvas) {
        for (int a = 0; a <= 360; a += Mill_SEPARATION_ANGLE) {
            millStartX = Math.cos(Math.toRadians(a + mOffsetSpin)) * (sunRadius - SPACE_MILL + MILL_LINE_LENGTH + mMillPaint.getStrokeWidth()) + getWidth() * .5f;
            millStartY = Math.sin(Math.toRadians(a + mOffsetSpin)) * (sunRadius - SPACE_MILL + MILL_LINE_LENGTH + mMillPaint.getStrokeWidth()) + mLineStartY;

            millStopX = Math.cos(Math.toRadians(a + mOffsetSpin)) * (sunRadius + SPACE_MILL + MILL_LINE_LENGTH + mMillPaint.getStrokeWidth()) + getWidth() * .5f;
            millStopY = Math.sin(Math.toRadians(a + mOffsetSpin)) * (sunRadius + SPACE_MILL + MILL_LINE_LENGTH + mMillPaint.getStrokeWidth()) + mLineStartY;
            canvas.drawLine((float) millStartX, (float) millStartY, (float) millStopX, (float) millStopY, mPaint);
        }
    }

    /**
     * Drawn Radius
     *
     * @param canvas
     */
    private void drawRadiusCenter(Canvas canvas) {
        for (int a = 0; a <= 360; a += Mill_SEPARATION_ANGLE) {
            millStartX = calculateCenter();
            millStartY = mLineStartY;

            millStopX = Math.cos(Math.toRadians(a + mOffsetSpin)) * (sunRadius / 2) + getWidth() * .5f;
            millStopY = Math.sin(Math.toRadians(a + mOffsetSpin)) * (sunRadius / 2) + mLineStartY;
            canvas.drawLine((float) millStartX, (float) millStartY, (float) millStopX, (float) millStopY, mPaint);
        }
    }

    /**
     * Drawn Wave
     *
     * @param canvas
     */
    private void drawWave(Canvas canvas) {
        mPathPaint.setColor(mWaveColor);
        float CurMidY = mHeight * (mMaxProgress - mCurrentProgress) / mMaxProgress;
        if (mCurY > CurMidY) {
            mCurY = mCurY - (mCurY - CurMidY) / 10;
        }
        mPath.reset();
        mPath.moveTo(0 - mDistance, mCurY);

        int waveNum = mWidth / ((int) WAVE_HALF_WIDTH * 4) + 1;
        int multiplier = 0;
        for (int i = 0; i < waveNum; i++) {
            mPath.quadTo(WAVE_HALF_WIDTH * (multiplier + 1) - mDistance, mCurY - WAVE_HEIGHT, WAVE_HALF_WIDTH * (multiplier + 2) - mDistance, mCurY);
            mPath.quadTo(WAVE_HALF_WIDTH * (multiplier + 3) - mDistance, mCurY + WAVE_HEIGHT, WAVE_HALF_WIDTH * (multiplier + 4) - mDistance, mCurY);
            multiplier += 4;
        }
        mDistance += WAVE_HALF_WIDTH / WAVE_SPEED;
        mDistance = mDistance % (WAVE_HALF_WIDTH * 4);

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);
    }
}
