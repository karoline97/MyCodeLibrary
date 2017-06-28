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

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/6/13.
 */

public class RailWayBar extends View {
    private Context mContext;
    private Paint textPaint;
    private Paint rectPaint;
    private Paint linePaint;

    private WeakReference<Bitmap> bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private float rectHeight;
    private int barWidth,barHeight;
    private List<FinacialData> datas;

    private int finshedColor = 0;
    private int targetColor = 0;
    private int excessColor = 0;

    private int lengedHeight = 0;
    private boolean isLengedVisible = false;
    private String desc,color1,desc1,color2,desc2;

    public RailWayBar(Context context) {
        super(context);
        init(context);
    }

    public RailWayBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RailWayBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        finshedColor = Color.parseColor("#a6baff");
        targetColor = Color.parseColor("#eceff1");
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
        setBarHeight(40);
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
        requestLayout();
        invalidate();
    }

    public void setLenged(String desc,String color1,String desc1,String color2,String desc2){
        isLengedVisible = true;
        lengedHeight = (int) SizeUtils.dp2px(mContext,56);
        this.desc = desc;
        this.color1 = color1;
        this.desc1 = desc1;
        this.color2 = color2;
        this.desc2 = desc2;

        setMeasuredDimension(onWidthMeasure(getMeasuredWidth()),onHeightMeasure(getMeasuredHeight()));
    }

    private void drawLenged(){
        float desc1startX = distance;
        float desc2startX = barWidth/2;
        Rect rect = new Rect();
        textPaint.setTextSize(SizeUtils.dp2px(mContext,14));
        textPaint.getTextBounds(desc,0,desc.length(),rect);

        bitmapCanvas.drawText(desc,desc1startX,lengedHeight/2+rect.height()/2,textPaint);

        int lgdRecW = (int)(distance - distance/4) ;
        rectPaint.setColor(Color.parseColor(color1));
        bitmapCanvas.drawRect(desc2startX,lengedHeight/2-lgdRecW/3,desc2startX+lgdRecW,
                lengedHeight/2+lgdRecW/3* 2,rectPaint);
        textPaint.getTextBounds(desc1,0,desc1.length(),rect);
        bitmapCanvas.drawText(desc1,desc2startX+ lgdRecW +4,lengedHeight/2+rect.height()/2,textPaint);

        rectPaint.setColor(Color.parseColor(color2));
        float d2startX = desc2startX + lgdRecW  + 4 + rect.width() +4;
        bitmapCanvas.drawRect(d2startX,lengedHeight/2-lgdRecW/3,d2startX+lgdRecW,
                lengedHeight/2 + lgdRecW/3*2,rectPaint);
        bitmapCanvas.drawText(desc2,d2startX+lgdRecW + 4,lengedHeight/2+rect.height()/2,textPaint);

        bitmapCanvas.save();
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
        int width = getMeasuredWidth();
        int hight = getMeasuredHeight();

        if (bitmapBuffer == null
                || (bitmapBuffer.get().getWidth() != width)
                || (bitmapBuffer.get().getHeight() != hight)) {

            if (width > 0 && hight > 0) {

                bitmapBuffer = new WeakReference<Bitmap>(Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_4444));
                bitmapCanvas = new Canvas(bitmapBuffer.get());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.save();
        bitmapBuffer.get().eraseColor(Color.TRANSPARENT);
        if(isLengedVisible){
            drawLenged();
        }

        float desc1startX = distance;
        float desc2startX = barWidth/3*2;
        Float startY = lengedHeight + 0f,endY,perWidth;
        if(datas == null) return;

        textPaint.setTextSize(SizeUtils.dp2px(mContext,13));
        for(int i=0;i< datas.size();i++){
           // startY = space*(i+1) + rectHeight*i + lengedHeight;
            endY = startY + rectHeight ;

            if(datas.get(i).targetV.contains("--") || datas.get(i).targetV.contains("-")){
                bitmapCanvas.drawLine(0,startY,barWidth,startY,linePaint);
                rectPaint.setColor(finshedColor);
                bitmapCanvas.drawRect(0,startY,distance/4,endY,rectPaint);
                bitmapCanvas.drawLine(0,endY,barWidth,endY,linePaint);
            }else {
                perWidth = Math.abs(Float.valueOf(datas.get(i).targetV))/barWidth;
                rectPaint.setColor(targetColor);
                bitmapCanvas.drawRect(0,startY,barWidth,endY,rectPaint);

               rectPaint.setColor(finshedColor);
                bitmapCanvas.drawRect(0,startY,(Math.abs(Float.valueOf(datas.get(i).finishedV))/perWidth),
                        endY,rectPaint);

            }

            Rect rect = new Rect();
            textPaint.getTextBounds(datas.get(i).desc,0,datas.get(i).desc.length(),rect);
            bitmapCanvas.drawText(datas.get(i).desc,desc1startX,startY+rectHeight/2+rect.height()/2,textPaint);
            bitmapCanvas.drawText(datas.get(i).finishedV+"万元",desc2startX,startY+rectHeight/2+rect.height()/2,textPaint);

            startY = endY +distance/4;
        }
        bitmapCanvas.save();

       // Rect displayRect = new Rect(0,0,barWidth,barHeight);
        //Rect det = new Rect(0,0,getWidth(),getHeight());

        canvas.drawBitmap(bitmapBuffer.get(),0,0,null);
        canvas.restore();
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
        int minSize = (int)SizeUtils.dp2px(mContext,16);
        if(mode1 == MeasureSpec.EXACTLY){
            barHeight = size1;
        }else {
            if(datas != null && datas.size()>0){
                barHeight = (int) (rectHeight + distance/4 )*datas.size() + lengedHeight + (int) distance;
            }else {
                barHeight = minSize - getPaddingTop() - getPaddingBottom();
            }
        }

        return barHeight;
    }
}
