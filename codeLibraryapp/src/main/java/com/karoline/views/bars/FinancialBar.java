package com.karoline.views.bars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.karoline.beans.FinacialData;
import com.karoline.utils.SizeUtils;

import java.util.List;

/**
 * Created by ${Karoline} on 2017/4/19.
 */

public class FinancialBar extends View{
    private Context mContext;
    private Paint textPaint;
    private Paint rectPaint;
    private Paint linePaint;

    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private float rectHeight;
    private int barWidth,barHeight;
    private List<FinacialData> datas;

    private int finshedColor = 0;
    private int targetColor = 0;
    private int excessColor = 0;

    public FinancialBar(Context context) {
        super(context);
    }

    public FinancialBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        finshedColor = Color.parseColor("#a6baff");
        targetColor = Color.parseColor("#fde0dc");
        excessColor = Color.parseColor("#f36c60");

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(SizeUtils.dp2px(context,13));

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(finshedColor);
        rectPaint.setTextSize(rectHeight);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.DKGRAY);
        linePaint .setTextSize(3);

        distance = SizeUtils.dp2px(context,16);
        setBarHeight(36);
    }

    public FinancialBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBarHeight(float rectHeighDp){
        if(rectHeighDp < 8){
            rectHeight = SizeUtils.dp2px(mContext,16);
        }else {
            this.rectHeight = SizeUtils.dp2px(mContext,rectHeighDp);
        }
        rectPaint.setTextSize(rectHeight);
    }

    public void setData(List<FinacialData> dataS){
        this.datas = dataS;
        invalidate();
    }

    public void setLenged(String desc,String desc1,String desc2){

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
            int width = getMeasuredWidth();
            int hight = getMeasuredHeight();
            bitmapBuffer = Bitmap.createBitmap(width,hight, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmapBuffer);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float space = distance/4;
        float desc1startX = distance;
        float desc2startX = barWidth/2;
        Float startY,endY,perWidth;

        if(datas == null) return;

        for(int i=0;i<datas.size();i++){
            startY = space*(i+1) + rectHeight*i;
            endY = startY + rectHeight;

            if(datas.get(i).targetV.contains("--")){
                bitmapCanvas.drawLine(0,startY,barWidth,startY,linePaint);
                rectPaint.setColor(finshedColor);
                bitmapCanvas.drawRect(0,startY,distance/4,endY,rectPaint);
                bitmapCanvas.drawLine(0,endY,barWidth,endY,linePaint);
            }else {
                perWidth = barWidth/(Math.abs(Float.valueOf(datas.get(i).targetV)));
                rectPaint.setColor(targetColor);
                bitmapCanvas.drawRect(0,startY,barWidth,endY,rectPaint);

                rectPaint.setColor(finshedColor);
                bitmapCanvas.drawRect(0,startY,perWidth*(Math.abs(Float.valueOf(datas.get(i).finishedV))),
                        endY,rectPaint);

                if(datas.get(i).getRate() > 1){
                    rectPaint.setColor(excessColor);
                    Float tempWid = (datas.get(i).getRate() - 1) * (Math.abs(Float.valueOf(datas.get(i).finishedV)));
                    bitmapCanvas.drawRect(barWidth - tempWid,startY,barWidth,endY,rectPaint);
                }
            }

            Rect rect = new Rect();
            textPaint.getTextBounds(datas.get(i).desc,0,datas.get(i).desc.length(),rect);
            bitmapCanvas.drawText(datas.get(i).desc,desc1startX,startY+rectHeight/2+rect.height()/2,textPaint);
            bitmapCanvas.drawText(datas.get(i).getDataDesc(),desc2startX,startY+rectHeight/2+rect.height()/2,textPaint);

        }

        bitmapCanvas.save();

        canvas.drawBitmap(bitmapBuffer,0,0,null);
        bitmapCanvas.restore();
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
        int minSize = (int) SizeUtils.dp2px(mContext,200);
        if(mode1 == MeasureSpec.EXACTLY){
            barHeight = size1;
        }else {
            if(datas != null && datas.size()>0){
                barHeight = (int) (rectHeight + distance/4)*datas.size() + (int) distance;
            }else {
                barHeight = minSize - getPaddingTop() - getPaddingBottom();
            }
        }

        return barHeight;
    }
}
