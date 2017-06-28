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

/**
 * Created by ${Karoline} on 2017/4/19.
 */

public class NationBarView extends View{
    private Context mContext;
    private Paint textPaint;
    private Paint rectPaint;
    private Bitmap bitmapBuffer;
    private Canvas bitmapCanvas;

    private float rectHeight,total,count1,count2;
    private int barWidth,barHeight,barCount;
    private float distance;

    public NationBarView(Context context) {
        super(context);
    }

    public NationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(SizeUtils.dp2px(context,16));

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#8d6e63"));
        rectPaint.setTextSize(rectHeight);

        distance = SizeUtils.dp2px(context,16);
        setBarWidth(0);
    }

    public NationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBarWidth(float rectHeightDp){
        if(rectHeightDp < 8){
            rectHeight = SizeUtils.dp2px(mContext,16);
        }else{
            rectHeight = SizeUtils.dp2px(mContext,rectHeightDp);
        }

        rectPaint.setTextSize(rectHeight);
    }

    public void setData(float count1,float count2,float total){
        this.count1 = count1;
        this.count2 = count2;
        this.total = total;
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

        if(count1 == 0 || count2 == 0 || total == 0){
            return;
        }

        float perWidth = barWidth/4;
        float perRectWidth = (perWidth * 2)/total;

        //文字
        textPaint.setTextSize(SizeUtils.dp2px(mContext,16));
        Rect rect = new Rect();
        String string = "汉族";
        textPaint.getTextBounds(string,0,string.length(),rect);
        bitmapCanvas.drawText(string,perWidth-distance-rect.width(),barHeight/2+rect.height()/2,textPaint);

        Rect rect1 = new Rect();
        String string1 = "少数民族";
        textPaint.getTextBounds(string1,0,string1.length(),rect1);
        bitmapCanvas.drawText(string1,perWidth*3+distance,barHeight/2+rect1.height()/2,textPaint);
        bitmapCanvas.save();

        //柱形图
        float rate1 = count1/total;
        float rate2 = count2/total;
        float markWidth = SizeUtils.dp2px(mContext,4);
        float end = perWidth + perRectWidth*count1;
        rectPaint.setColor(Color.parseColor("#8d6e63"));
        RectF rectRate1 = new RectF(perWidth,barHeight/2-rectHeight/2,end,barHeight/2+rectHeight/2);
        bitmapCanvas.drawRect(rectRate1,rectPaint);
        bitmapCanvas.save();

        textPaint.setTextSize(SizeUtils.dp2px(mContext,10));
        bitmapCanvas.drawRect((end-perRectWidth*count1/2)-markWidth/2,
                barHeight/2-rectHeight/2-distance/2,(end-perRectWidth*count1/2)+markWidth/2,
                barHeight/2-rectHeight/2,rectPaint);
        String rate1S = ((int)count1) +"人，"+"占比"+floatToint(rate1);
        Rect rect2 = new Rect();
        textPaint.getTextBounds(rate1S,0,rate1S.length(),rect2);
        bitmapCanvas.drawText(rate1S,(end-perRectWidth*count1/2)-rect2.width()/2,
                barHeight/2-rectHeight/2-distance/2-distance/4,textPaint);
        bitmapCanvas.save();

        rectPaint.setColor(Color.parseColor("#d7ccc8"));
        float start2 = perRectWidth*count1 +perWidth;
        RectF rectRate2 = new RectF(start2,barHeight/2-rectHeight/2,perWidth*3,barHeight/2+rectHeight/2);
        bitmapCanvas.drawRect(rectRate2,rectPaint);
        bitmapCanvas.save();

        float start3 = (perWidth*3-start2)/2 + start2;
        bitmapCanvas.drawRect(start3-markWidth/2,
                barHeight/2+rectHeight/2,start3+markWidth/2,
                barHeight/2+rectHeight/2+distance/2,rectPaint);
        String rate2S = ((int)count2)+"人，"+"占比"+floatToint(rate2);
        Rect rect3 = new Rect();
        textPaint.getTextBounds(rate2S,0,rate2S.length(),rect3);
        bitmapCanvas.drawText(rate2S,start3-rect3.width()/2,
                barHeight/2+rectHeight/2+distance/2+rect3.height()+distance/4,textPaint);
        bitmapCanvas.save();

        canvas.drawBitmap(bitmapBuffer,0,0,null);
        bitmapCanvas.restore();
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

    private String floatToint(float count){
        String string = String.valueOf(count*100);
        int index = string.indexOf(".");
        if(index > 0){
            String temp = string.substring(index+1,index+2);
            String temp1 = string.substring(0,index);
            if(Integer.valueOf(temp) >= 5){
                return Integer.valueOf(temp1)+1+"%";
            }else {
                return Integer.valueOf(temp1)+"%";
            }
        }else {
            return (int)count*100 +"%";
        }
    }
}
