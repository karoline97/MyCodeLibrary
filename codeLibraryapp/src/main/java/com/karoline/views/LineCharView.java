package com.karoline.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.china317.developlibrary.utils.DisplayUtil;
import com.china317.syrailway.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class LineCharView extends View {
    private Context mContext;

    private Paint mLinePaint;
    private Paint mTextPaint;

    private Paint mOnePaint;
    private Paint mTwoPaint;

    private int lineColor;
    private int otherLineColor;

    private int mViewHeight;
    private int mViewWidth;

    private List<String> mDate;
    private List<String> mMonth;
    private List<Float> datas;
    //private List<Float> otherDatas;

    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;

    private int startXLeftMargin = 30;
    private int startYBottomMargin = 16;
    private int endXRightMargin = 16;
    private int enyTopMargin = 20;
    private int linePaddingText = 10;

    private float textSize;

    private float startX;
    private float startY;
    private float endX;
    private float endY;

    private Rect rect;
    private float maxNum;
    private float perY;
    private float perX;
    private float mStartX;


    public LineCharView(Context context) {
        this(context, null);
    }

    public LineCharView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineCharView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LineCharView);
        lineColor = typedArray.getColor(R.styleable.LineCharView_oneLineColor, Color.BLUE);
        otherLineColor = typedArray.getColor(R.styleable.LineCharView_otherLineColor, Color.RED);
        textSize = typedArray.getDimension(R.styleable.LineCharView_textSize, DisplayUtil.DipToPx(mContext, 10));
        init();
    }

    private void init() {
        startXLeftMargin = 30;
        startYBottomMargin = 30;
        endXRightMargin = 16;
        enyTopMargin = 20;
        linePaddingText = 10;

        mDate = new ArrayList<>();
        mDate.add("0%");
        mDate.add("10%");
        mDate.add("20%");
        mDate.add("30%");
        mDate.add("40%");
        mDate.add("50%");
        mDate.add("60%");
        mDate.add("70%");
        mDate.add("80%");
        mDate.add("90%");
        mDate.add("100%");

        mOnePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOnePaint.setColor(lineColor);
        mOnePaint.setStrokeWidth(5);

        mTwoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTwoPaint.setColor(otherLineColor);
        mTwoPaint.setStrokeWidth(5);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(onWidthMeasure(widthMeasureSpec), onHeightMeasure(heightMeasureSpec));
    }

    private int onWidthMeasure(int width){
        int mode = MeasureSpec.getMode(width);
        int size = MeasureSpec.getSize(width);

        if(mode == MeasureSpec.EXACTLY){
            mViewWidth = size;
        }else if(mode == MeasureSpec.AT_MOST){
            mViewWidth = width - getPaddingLeft() - getPaddingRight();
        }
        return mViewWidth;
    }


    private int onHeightMeasure(int height){
        int mode1 = MeasureSpec.getMode(height);
        int size1 = MeasureSpec.getSize(height);
        int minSize = (int) DisplayUtil.DipToPx(mContext,120);
        int temp1 = 0,temp2 = 0;
        if(mode1 == MeasureSpec.EXACTLY){
            mViewHeight = size1;
        }else {
            if(datas != null && datas.size()>0){
                temp1 = (int) (DisplayUtil.DipToPx(mContext, 30))*datas.size()
                        + (int) DisplayUtil.DipToPx(mContext, 16)*2;
            }else {
                temp2 = minSize - getPaddingTop() - getPaddingBottom();
            }

            mViewHeight = Math.max(temp1,temp2);
        }

        return mViewHeight;
    }


    public void initDatas(List<Float> nums, List<String> monthes) {
        this.datas = nums;
        this.mMonth = monthes;
        this.maxNum = Collections.max(nums);

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmapBuffer == null) {
            bitmapBuffer = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmapBuffer);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(datas  == null || mMonth == null){
            return;
        }

        startX = DisplayUtil.DipToPx(mContext, startXLeftMargin);//x轴的起点
        startY = mViewHeight - DisplayUtil.DipToPx(mContext, startYBottomMargin);//Y轴的起点

        endX = mViewWidth - DisplayUtil.DipToPx(mContext, endXRightMargin);
        endY = DisplayUtil.DipToPx(mContext, enyTopMargin);
        //画x轴
        initY();
        //画Y轴
        initX();
        initCircleAndLine();
        initText();

        bitmapCanvas.save();
        canvas.drawBitmap(bitmapBuffer, 0, 0, null);

    }

    private void initX() {
        perX = (endX - startX - DisplayUtil.DipToPx(mContext, 32))/(mMonth.size()-1);
        mStartX = startX +  DisplayUtil.DipToPx(mContext, 16);

        for (int i = 0; i < mMonth.size(); i++) {
            Rect rect = new Rect();
            mTextPaint.getTextBounds(mMonth.get(i), 0, mMonth.get(i).length(), rect);

            bitmapCanvas.drawText(mMonth.get(i),
                    perX*i + mStartX,
                    startY - rect.height() / 2, mTextPaint);
        }
    }


    private void initY() {
        perY = (startY - endY - endY)/maxNum;
        for (int i = 0; i < mDate.size(); i++) {
            float itemY = startY - maxNum/100 * i*10 *perY -endY;
            bitmapCanvas.drawLine(startX + linePaddingText, itemY,
                    endX, itemY, mLinePaint);

            rect = new Rect();
            mTextPaint.getTextBounds(mDate.get(i), 0, mDate.get(i).length(), rect);

            bitmapCanvas.drawText(mDate.get(i), startX - rect.width() / 2,
                    itemY + rect.height() / 2, mTextPaint);
        }

        bitmapCanvas.save();
    }


    private void initCircleAndLine() {
        if (mDate == null || mDate.size() == 0 ){//|| otherDatas == null || otherDatas.size() == 0) {
            return;
        }

        float oldX = 0,oldY=0,newX,newY;
        for (int i = 0; i < mMonth.size(); i++) {
            newX = mStartX +perX*i;
            newY = startY -(perY * datas.get(i))-endY;
            bitmapCanvas.drawCircle(newX, newY, 5, mTextPaint);
            if(oldX>0 && oldY>0){
                mOnePaint.setColor(lineColor);
                bitmapCanvas.drawLine(oldX,oldY,newX,newY,mOnePaint);
            }
            oldX = newX;
            oldY = newY;
        }
    }


    private void initText() {

        bitmapCanvas.drawLine(endX / 2 - 100, startY + 30, endX / 2 - 70, startY + 30, mOnePaint);
        bitmapCanvas.drawLine(endX / 2 + 40, startY + 30, endX / 2 + 70, startY + 30, mTwoPaint);

        mOnePaint.setTextSize(20);
        mOnePaint.setColor(Color.BLACK);
        bitmapCanvas.drawText("执行情况", endX / 2 - 60, startY + 38, mOnePaint);

        mTwoPaint.setTextSize(20);
        mOnePaint.setColor(Color.BLACK);
        bitmapCanvas.drawText("合同总价", endX / 2 + 80, startY + 38, mOnePaint);

    }


}
