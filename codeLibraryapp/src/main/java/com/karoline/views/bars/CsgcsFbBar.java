package com.karoline.views.bars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.karoline.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/5/11.
 */

public class CsgcsFbBar extends View{
    private Context mContext;
    private Paint mPaint;
    private Paint mTextPaint;

    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private float rectDis;
    private float rectHeight,textSize,LineSize;
    private int barWidth,barHeight;
    private List<CsgcsData> datas;

    private int textColor,lineColor,rectColor,xMax;
    private List<String> xDescList;

    public CsgcsFbBar(Context context) {
        super(context);
    }

    public CsgcsFbBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        xDescList = new ArrayList<>();
        textColor = Color.BLACK;
        lineColor = Color.LTGRAY;
        rectColor = Color.parseColor("#91a7ff");

        textSize = SizeUtils.dp2px(context,13);
        LineSize = SizeUtils.dp2px(context,1);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        distance = SizeUtils.dp2px(context,16);
        rectDis = distance;
        setBarHeight(24);
    }

    public CsgcsFbBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBarHeight(float rectHeighDp){
        if(rectHeighDp < 8){
            rectHeight = SizeUtils.dp2px(mContext,16);
        }else {
            this.rectHeight = SizeUtils.dp2px(mContext,rectHeighDp);
        }
        mPaint.setTextSize(rectHeight);
    }

    public void setData(List<CsgcsData> dataS,int xMax){
        this.xMax = xMax;
        this.datas = dataS;
        int temp = xMax % 10;
        int temp2 = xMax /10;
        int totalX = 0;
        if(temp > 0){
            totalX = temp2 +1;
        }else {
            totalX = temp2;
        }
        xDescList.clear();
        for(int i = 0 ; i <= totalX ; i++){
            xDescList.add(String.valueOf(i*10));
        }

        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int Width = onWidthMeasure(widthMeasureSpec);
        int height = onHeightMeasure(heightMeasureSpec);
        setMeasuredDimension(Width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(bitmapBuffer == null){
            bitmapBuffer = Bitmap.createBitmap(barWidth,barHeight, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmapBuffer);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(datas == null || datas.size() == 0){
            return;
        }
        if(xDescList == null || xDescList.size() ==0){
            return;
        }

        float xStart = SizeUtils.dp2px(mContext,56);
        float xEnd = barWidth - distance;
        float yEnd = barHeight - SizeUtils.dp2px(mContext,24);
        float yStart = distance;

        float perW = (xEnd - xStart - distance)/(xDescList.size()-1);
        float perH = (yEnd - yStart - distance)/datas.size();
        //x轴，y轴
        mPaint.setTextSize(LineSize);
        mPaint.setColor(lineColor);
        bitmapCanvas.drawLine(xStart,yEnd,xEnd,yEnd,mPaint);
        bitmapCanvas.drawLine(xStart,yEnd,xStart,yStart,mPaint);

        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        for(int i=0;i< xDescList.size();i++) {
            Rect rect = new Rect();
            mTextPaint.getTextBounds(xDescList.get(i), 0, xDescList.get(i).length(), rect);
            if (i == 0) {
                bitmapCanvas.drawText(xDescList.get(i), xStart - rect.width() / 2,
                        yEnd + distance / 4 + rect.height(), mTextPaint);
            } else {
                bitmapCanvas.drawLine(i * perW + xStart, yEnd, i* perW + xStart, yStart, mPaint);
                bitmapCanvas.drawText(xDescList.get(i), i * perW + xStart - rect.width() / 2,
                        yEnd + distance / 4+ rect.height(), mTextPaint);
            }
        }
        bitmapCanvas.save();

        float yCenter = 0;
        float perbarW = (xEnd - xStart - distance) / xMax;
        mPaint.setTextSize(rectHeight);
        mPaint.setColor(rectColor);
        for(int i = 0;i < datas.size();i++) {
            Rect rect = new Rect();
            String desc = datas.get(i).getDesc();
            mTextPaint.getTextBounds(desc, 0, desc.length(), rect);

            yCenter = yEnd - distance - rectDis/2 - i*perH;
            mTextPaint.setColor(textColor);
            bitmapCanvas.drawText(desc,xStart - distance/4 - rect.width(),
                    yCenter+ rect.height()/2,mTextPaint);

            bitmapCanvas.drawRect(xStart,yCenter + rectHeight/2,
                    xStart+perbarW*((float) datas.get(i).getCount()),
                    yCenter - rectHeight/2,mPaint);

            Rect rect1 = new Rect();
            String count = String.valueOf(datas.get(i).getCount());
            String counD = count.substring(0,count.indexOf("."));
            mTextPaint.getTextBounds(counD, 0, counD.length(), rect1);
            mTextPaint.setColor(Color.WHITE);
            bitmapCanvas.drawText(counD,
                    xStart+perbarW*((float) datas.get(i).getCount()) -distance/2-rect1.width(),
                    yCenter + rect1.height()/2,mTextPaint);

        }
        bitmapCanvas.save();

        canvas.drawBitmap(bitmapBuffer,0,0,null);

    }

    private int onWidthMeasure(int width){
        int mode = MeasureSpec.getMode(width);
        int size = MeasureSpec.getSize(width);

        if(mode == MeasureSpec.EXACTLY){
            barWidth = size;
        }else if(mode == MeasureSpec.AT_MOST){
            barWidth = width - getPaddingLeft() - getPaddingRight();
        }
        return barWidth;
    }


    private int onHeightMeasure(int height){
        int mode1 = MeasureSpec.getMode(height);
        int size1 = MeasureSpec.getSize(height);
        int minSize = (int) SizeUtils.dp2px(mContext,120);
        int temp1 = 0,temp2 = 0;
        if(mode1 == MeasureSpec.EXACTLY){
            barHeight = size1;
        }else {
            if(datas != null && datas.size()>0){
                temp1 = (int) (rectHeight + rectDis)*datas.size() + (int) distance*2;
            }else {
                temp2 = minSize - getPaddingTop() - getPaddingBottom();
            }

            barHeight = Math.max(temp1,temp2);
        }

        return barHeight;
    }

}
