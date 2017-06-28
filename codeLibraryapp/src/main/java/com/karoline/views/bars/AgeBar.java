package com.karoline.views.bars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.karoline.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/4/19.
 */

public class AgeBar extends View {
    private Context mContext;
    private Paint textPaint;
    private Paint linePaint;
    private Paint rectPaint;

    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private float rectWidth,maxValue;
    private int barWidth,barHeight;
    private List<Integer> yValues;
    private List<AgeData> datas;

    public AgeBar(Context context) {
        super(context);
    }

    public AgeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(SizeUtils.dp2px(context,10));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.DKGRAY);
        linePaint .setTextSize(3);


        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#91a7ff"));
        rectPaint.setTextSize(rectWidth);

        distance = SizeUtils.dp2px(context,16);
        setBarWidth(24);
    }

    public AgeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBarWidth(float rectWidthDp){
        if(rectWidthDp < 8){
            rectWidth = SizeUtils.dp2px(mContext,16);
        }else {
            this.rectWidth = SizeUtils.dp2px(mContext,rectWidthDp);
        }
        rectPaint.setTextSize(rectWidth);
    }

    private void setyValues(){
        if(datas == null || datas.size()==0) return;
        if(yValues == null) {
            yValues = new ArrayList<>();
        }
        yValues.clear();
        for(AgeData data : datas){
            yValues.add(data.count);
        }
        int ageNumMax = Collections.max(yValues);
        int temp = ageNumMax % 10;
        int ageYsize = 0;
        int temp1 = (int) ageNumMax/10;
        if(temp > 0){
            ageYsize = temp1 +1;
        }else{
            ageYsize = temp1;
        }
        yValues.clear();
        for(int i=0;i<ageYsize;i++){
            yValues.add((i+1)*10);
        }
        maxValue = ageYsize*10+10;
    }

    public void setData(List<AgeData> dataS){
        this.datas = dataS;
        setyValues();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onBarMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(barWidth,barHeight);
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
        if(yValues == null || datas == null || datas.size() == 0 ) {
            return;
        }
        //Y
        float yTextEndX = distance;
        float yTextStartY = barHeight - distance;
        float perY = yTextStartY/maxValue;

        textPaint.setTextSize(SizeUtils.dp2px(mContext,10));
        for(int i=0;i <= yValues.size();i++){
            if(i == yValues.size()){
                bitmapCanvas.drawText("0",distance/2, yTextStartY,textPaint);
            }else {
                Rect rect1 = new Rect();
                String yString = String.valueOf(yValues.get(i));
                textPaint.getTextBounds(yString, 0, yString.length(), rect1);
                bitmapCanvas.drawText(yString,yTextEndX - rect1.width()-5,
                        yTextStartY - perY*yValues.get(i) + rect1.height()/2,textPaint); //坐标值

                bitmapCanvas.drawLine(yTextEndX,yTextStartY - perY*yValues.get(i),
                        barWidth,yTextStartY - perY*yValues.get(i),linePaint); //横线
            }
        }

        bitmapCanvas.drawLine(yTextEndX,0,yTextEndX,yTextStartY,linePaint);//Y轴
        bitmapCanvas.save();

        //X
        textPaint.setTextSize(SizeUtils.dp2px(mContext,10));
        float xTendX = barWidth - distance *2;
        float perBar = xTendX/datas.size();
        for(int i = 0; i < datas.size(); i++){
            Rect rect1 = new Rect();
            textPaint.getTextBounds(datas.get(i).desc, 0, datas.get(i).desc.length(), rect1);
            bitmapCanvas.drawText(datas.get(i).desc,perBar*(i+1) - rect1.width()/2,
                    yTextStartY + distance/1.5f,textPaint); //坐标值

            float barH = perY * datas.get(i).count;
            RectF rectF = new RectF(perBar*(i+1)-rectWidth/2,yTextStartY - barH,
                    perBar*(i+1)+rectWidth/2,yTextStartY);
            bitmapCanvas.drawRect(rectF,rectPaint);//柱形图

            Rect rect2 = new Rect();
            textPaint.getTextBounds(String.valueOf(datas.get(i).count),0,
                    String.valueOf(datas.get(i).count).length(),rect2);
            bitmapCanvas.drawText(String.valueOf(datas.get(i).count),perBar*(i+1) - rect2.width()/2,
                    yTextStartY - barH - 5,textPaint); //柱形图上的值
        }

        bitmapCanvas.drawLine(yTextEndX,yTextStartY,barWidth,yTextStartY,linePaint);//X轴
        bitmapCanvas.save();

        textPaint.setTextSize(SizeUtils.dp2px(mContext,16));
        bitmapCanvas.drawText("年龄",xTendX/2,distance,textPaint);
        bitmapCanvas.save();

        canvas.drawBitmap(bitmapBuffer,0,0,null);
    }

    private void onBarMeasure(int width,int height){
        int mode = MeasureSpec.getMode(width);
        int size = MeasureSpec.getSize(width);
        if(mode == MeasureSpec.EXACTLY){
            barWidth = size;
        }else if(mode == MeasureSpec.AT_MOST){
            barWidth = width - getPaddingLeft() - getPaddingRight();
        }

        int mode1 = MeasureSpec.getMode(height);
        int size1 = MeasureSpec.getSize(height);
        if(mode1 == MeasureSpec.EXACTLY){
            barHeight = size1;
        }else if(mode1 == MeasureSpec.AT_MOST){
            barHeight = height - getPaddingTop() - getPaddingBottom();
        }
    }
}
