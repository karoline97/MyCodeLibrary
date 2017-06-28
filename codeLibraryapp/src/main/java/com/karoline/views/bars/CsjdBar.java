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
 * Created by ${Karoline} on 2017/5/22.
 */

public class CsjdBar extends View {
    private Context mContext;
    private Paint mPaint;
    private Paint mTextPaint;

    private Bitmap bitBuffer;
    private Canvas bitCanvas;

    private float distance;
    private float rectDis;
    private float rectHeight,textSize,LineSize;
    private int barWidth,barHeight;
    private List<CsjdData> datas;

    private int textColor,lineColor,rect1Color,rect2Color, xMax;
    private List<String> xDescList;

    public CsjdBar(Context context) {
        super(context);
    }

    public CsjdBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        xDescList = new ArrayList<>();
        textColor = Color.BLACK;
        lineColor = Color.LTGRAY;
        rect1Color = Color.parseColor("#91a7ff");
        rect2Color = Color.parseColor("#ff9800");

        textSize = SizeUtils.dp2px(context,13);
        LineSize = SizeUtils.dp2px(context,1);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        distance = SizeUtils.dp2px(context,16);
        rectDis = distance;
        setBarHeight(24);
    }

    public CsjdBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setData(List<CsjdData> dataS,int xMax){
        //this.xMax = xMax;
        this.datas = dataS;
        int temp = xMax % 10;
        int temp2 = xMax /10;
        int totalX = 0;
        if(temp > 0){
            totalX = temp2 +1;
        }else {
            totalX = temp2;
        }
        this.xMax = totalX*10;
        xDescList.clear();
        for(int i = 0 ; i <= totalX ; i++){
            xDescList.add(String.valueOf(i*10));
        }

        setMeasuredDimension(onWidthMeasure(getMeasuredWidth()),onHeightMeasure(getMeasuredHeight()));
        onSizeChanged(barWidth,barHeight,0,0);
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        barWidth = onWidthMeasure(widthMeasureSpec);
        barHeight = onHeightMeasure(heightMeasureSpec);
        setMeasuredDimension(barWidth,barHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //if(bitBuffer == null){
            bitBuffer = Bitmap.createBitmap(barWidth,barHeight, Bitmap.Config.ARGB_8888);
            bitCanvas = new Canvas(bitBuffer);
       // }
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
        bitCanvas.drawLine(xStart,yEnd,xEnd,yEnd,mPaint);
        bitCanvas.drawLine(xStart,yEnd,xStart,yStart,mPaint);

        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        for(int i=0;i< xDescList.size();i++) {
            Rect rect = new Rect();
            mTextPaint.getTextBounds(xDescList.get(i), 0, xDescList.get(i).length(), rect);
            if (i == 0) {
                bitCanvas.drawText(xDescList.get(i), xStart - rect.width() / 2,
                        yEnd + distance / 4 + rect.height(), mTextPaint);
            } else {
                bitCanvas.drawLine(i * perW + xStart, yEnd, i* perW + xStart, yStart, mPaint);
                bitCanvas.drawText(xDescList.get(i), i * perW + xStart - rect.width() / 2,
                        yEnd + distance / 4+ rect.height(), mTextPaint);
            }
        }
        bitCanvas.save();

        float yCenter = 0;
        float perbarW = (xEnd - xStart - distance) / xMax;
        mPaint.setTextSize(rectHeight/2);
        for(int i = 0;i < datas.size();i++) {
            Rect rect = new Rect();
            String desc = datas.get(i).getName();
            mTextPaint.getTextBounds(desc, 0, desc.length(), rect);

            yCenter = yEnd - distance - rectDis/2 - i*perH;
            mTextPaint.setColor(textColor);
            if(desc.length() >8){
                desc = desc.substring(0,6)+"...";
            }
            if(desc.length()>4){
                String desct = desc.substring(0,4);
                mTextPaint.getTextBounds(desct, 0, desct.length(), rect);
                bitCanvas.drawText(desct,xStart - distance/4 - rect.width(),
                        yCenter,mTextPaint);

                String desct1 = desc.substring(4,desc.length());
                bitCanvas.drawText(desct1,0,yCenter+(rect.height()),mTextPaint);

            }else {
                bitCanvas.drawText(desc,xStart - distance/4 - rect.width(),
                        yCenter+ rect.height()/2,mTextPaint);
            }

            mPaint.setColor(rect1Color);
            bitCanvas.drawRect(xStart,yCenter + rectHeight/2,
                    xStart+perbarW*(datas.get(i).getTotal()),
                    yCenter,mPaint);

            mPaint.setColor(rect2Color);
            bitCanvas.drawRect(xStart,yCenter,
                    xStart+perbarW*(datas.get(i).getDone()),
                    yCenter - rectHeight/2,mPaint);

        }
        bitCanvas.save();

        canvas.drawBitmap(bitBuffer,0,0,null);
    }

    private int onWidthMeasure(int width){
        int mode = MeasureSpec.getMode(width);
        int size = MeasureSpec.getSize(width);
        int temW = 0;

        if(mode == MeasureSpec.EXACTLY){
            temW = size;
        }else if(mode == MeasureSpec.AT_MOST){
            temW = width - getPaddingLeft() - getPaddingRight();
        }
        return temW;
    }


    private int onHeightMeasure(int height){
        int mode1 = MeasureSpec.getMode(height);
        int size1 = MeasureSpec.getSize(height);
        int minSize = (int) SizeUtils.dp2px(mContext,120);
        int temp1 = 0,temp2 = 0 ,temH=0;
        if(mode1 == MeasureSpec.EXACTLY){
            temH = size1;
        }else {
            if(datas != null && datas.size()>0){
                temp1 = (int) (rectHeight + rectDis)*datas.size() + (int) distance*2;
            }else {
                temp2 = minSize - getPaddingTop() - getPaddingBottom();
            }

            temH = Math.max(temp1,temp2);
        }

        return temH;
    }
}
