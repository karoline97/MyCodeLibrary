package com.karoline.views.bars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.china317.developlibrary.utils.DisplayUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/6/14.
 */

public class AssetLenged extends View {
    private Context mContext;
    private Paint textPaint;
    private Paint rectPaint;

    private WeakReference<Bitmap> bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private int barWidth,barHeight;
    private List<Integer> colors;
    private List<String> descs;

    public AssetLenged(Context context) {
        super(context);
    }

    public AssetLenged(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(DisplayUtil.DipToPx(context,10));

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#91a7ff"));

        distance = DisplayUtil.DipToPx(context,16);
    }

    public AssetLenged(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<Integer> colors,List<String> descs){
        this.colors = colors;
        this.descs = descs;
        if(colors == null || colors.size() == 0){
            colors = new ArrayList<>();
            colors.add(Color.parseColor("#f36c60"));
        }
       // requestLayout();
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
        super.onDraw(canvas);

        canvas.save();
        bitmapBuffer.get().eraseColor(Color.TRANSPARENT);
        drawLenged();
        canvas.drawBitmap(bitmapBuffer.get(),0,0,null);
        canvas.restore();
    }

    private void drawLenged(){
        if(descs != null && descs.size() > 0){
            Rect rect = new Rect();
            float lengedX = distance;
            float lengedY = distance;
            float totalWidth;
            int count = 0;
            for(int i = 0;i<descs.size();i++){
                rectPaint.setColor(colors.get(i % colors.size()));
                textPaint.getTextBounds(descs.get(i),0,descs.get(i).length(),rect);
                if(!TextUtils.isEmpty(descs.get(i))){

                    totalWidth = lengedX + distance/2 + 4 + rect.width() +distance;
                    if(totalWidth > barWidth){
                        count ++;
                        lengedY = lengedY +distance;
                        lengedX = distance;
                    }

                    if(TextUtils.isEmpty(descs.get(i))) return;
                    bitmapCanvas.drawRect(lengedX,lengedY-distance/2,lengedX+distance/2,lengedY ,rectPaint);
                    lengedX = lengedX + distance/2 + 4;
                    bitmapCanvas.drawText(descs.get(i),lengedX,lengedY,textPaint);
                    lengedX = lengedX + rect.width() + distance;
                }
            }
        }
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