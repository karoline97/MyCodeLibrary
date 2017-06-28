package com.karoline.views.bars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.karoline.utils.SizeUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/6/14.
 */

public class AssetKcPie extends View {
    private Context mContext;
    private Paint textPaint;
    private Paint arcPaint;
    private Paint linePaint;

    private WeakReference<Bitmap> bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private float radius;
    private int barWidth,barHeight;
    private List<AssetKcData> datas;
    private List<AngleSE> angleSEs;
    private List<RectF> lengedRectes;
    private OnSelectedListener mListener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float actionX = event.getX();
            float actionY = event.getY();
            double distance = Math.sqrt(Math.pow(Math.abs(actionX-barWidth/2),2)+
                                Math.pow(Math.abs(actionY-barHeight/2),2));
            double angle = Math.atan((actionY-barHeight/2) /(actionX-barWidth/2)) /3.14 * 180 - 90;
            float X = barWidth/2,Y=(barHeight-lengedHeight)/2;
            if(actionX > X && actionY<Y){
                angle = 90-angle;
            } else if (actionX > X && actionY>Y) {
                angle = 90+angle;
            }else if (actionX < X && actionY>Y) {
                angle = 270-angle;
            }else if (actionX < X && actionY<Y) {
                angle = 270+angle;
            }

            if(angleSEs == null || angleSEs.size() == 0 || mListener == null) return false;

            for(int i=0;i<angleSEs.size();i++){
                if(distance <= radius){
                    if(angle > angleSEs.get(i).getStartAngle() && angle<angleSEs.get(i).getSweepAngle()){
                        mListener.onSelected(i);
                    }
                }else if(lengedRectes.get(i).contains(actionX,actionY)){
                    mListener.onSelected(i);
                }
            }
            return false;
        }

        return super.onTouchEvent(event);
    }

    private float totalNum;

    private int lengedHeight = 0;
    private boolean isLengedVisible = false;

    public AssetKcPie(Context context) {
        super(context);
    }

    public AssetKcPie(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(SizeUtils.dp2px(context,11));

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setTextSize(radius);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.DKGRAY);
        linePaint .setTextSize(3);

        distance = SizeUtils.dp2px(context,16);

        setRadius(SizeUtils.dp2px(context,80));

    }

    public AssetKcPie(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRadius(float rs){
        this.radius = rs;
    }

    public void setData(List<AssetKcData> dataS,float total){
        this.datas = dataS;
        this.totalNum = total;
        invalidate();
    }

    public void setLenged(){
        isLengedVisible = true;
        lengedHeight = (int) SizeUtils.dp2px(mContext,32);

        setMeasuredDimension(onWidthMeasure(getMeasuredWidth()),onHeightMeasure(getMeasuredHeight()));
    }

    private void drawLenged(){
        lengedRectes = new ArrayList<>();
        Rect rect = new Rect();
        float lengedX = distance;
        float lengedY = barHeight - lengedHeight + distance;
        float totalWidth;
        for(int i = 0;i<datas.size();i++){
            RectF rectF = new RectF();
            textPaint.setTextSize(SizeUtils.dp2px(mContext,13));
            textPaint.setFakeBoldText(true);
            textPaint.setShadowLayer(5,4,4,Color.GRAY);
            textPaint.getTextBounds(datas.get(i).getDesc(),0,datas.get(i).getDesc().length(),rect);
            arcPaint.setTextSize(distance/2);
            arcPaint.setColor(datas.get(i).getColor());

            if(!TextUtils.isEmpty(datas.get(i).getDesc())){

                totalWidth = lengedX + distance/2 + 4 + rect.width() +distance;
                if(totalWidth > barWidth){
                    lengedY = lengedY +distance;
                    lengedX = distance;
                }

                bitmapCanvas.drawRect(lengedX,lengedY-distance/2,lengedX+distance/2,lengedY ,arcPaint);
                rectF.left = lengedX;
                rectF.top = lengedY-distance;
                lengedX = lengedX + distance/2 + 4;
                bitmapCanvas.drawText(datas.get(i).getDesc(),lengedX,lengedY,textPaint);
                lengedX = lengedX + rect.width() + distance;
                rectF.right = lengedX;
                rectF.bottom = lengedY + distance;
                lengedRectes.add(rectF);
            }
        }

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
        super.onDraw(canvas);

        canvas.save();
        bitmapBuffer.get().eraseColor(Color.TRANSPARENT);

        if(datas != null && datas.size() > 0 ){
            if(isLengedVisible){
                drawLenged();
            }

            angleSEs = new ArrayList<>();

            RectF arcRect = new RectF(barWidth/2-radius,(barHeight-lengedHeight)/2 - radius,
                    barWidth/2+radius,(barHeight-lengedHeight)/2 + radius);
            float startAngle = -90,sweepAngle = 0;
            float perAngle = totalNum/360;
            String desc;
            float lineAngle;

            Rect rect = new Rect();

            for(int i=0;i<datas.size();i++){
                sweepAngle = datas.get(i).getNum()/perAngle;
                arcPaint.setColor(datas.get(i).getColor());
                bitmapCanvas.drawArc(arcRect,startAngle,sweepAngle,true,arcPaint);
                angleSEs.add(new AngleSE(startAngle,sweepAngle+startAngle));

                lineAngle = startAngle+ sweepAngle/2;
                desc = datas.get(i).getDesc()+","+datas.get(i).getSNum();
                drwaLineAndText(sweepAngle,lineAngle,desc,rect,i);

                startAngle += sweepAngle;
            }
            bitmapCanvas.save();
            bitmapCanvas.restore();
        }

        Rect displayRect = new Rect(0,0,barWidth,barHeight);
        Rect det = new Rect(0,0,getWidth(),getHeight());

        canvas.drawBitmap(bitmapBuffer.get(),displayRect,det,null);
        canvas.restore();
    }

    private void drwaLineAndText(float sweepAngle,float lineAngle,String desc,Rect rect,int i){
        float lineStartX,lineStartY ,lineEndX,lineEndY ;

        lineStartX   =   barWidth/2   +   (radius- distance)   *  (float) Math.cos(lineAngle *   3.14   /180 );
        lineStartY   =   (barHeight-lengedHeight)/2   +   (radius- distance)  *   (float) Math.sin(lineAngle   *   3.14/180);
        if(Math.abs(sweepAngle) <= 30){
            float num = (datas.size() - i)%3;
            lineEndX   =   barWidth/2   +   (radius+ distance*num*1f)   *  (float) Math.cos(lineAngle *   3.14   /180 );
            lineEndY   =   (barHeight-lengedHeight)/2   +   (radius+ distance*num*1f)  *   (float) Math.sin(lineAngle   *   3.14   /180);
        }else {
            lineEndX   =   barWidth/2   +   (radius+ distance)   *  (float) Math.cos(lineAngle *   3.14   /180 );
            lineEndY   =   (barHeight-lengedHeight)/2   +   (radius+ distance)  *   (float) Math.sin(lineAngle   *   3.14   /180);
        }
        bitmapCanvas.drawLine(lineStartX,lineStartY,lineEndX,lineEndY,linePaint);

        textPaint.getTextBounds(desc,0,desc.length(),rect);
        textPaint.setTextSize(SizeUtils.dp2px(mContext,11));
        textPaint.setFakeBoldText(false);
        textPaint.setShadowLayer(0,0,0,Color.TRANSPARENT);
        if (lineStartX>barWidth/2) {
            bitmapCanvas.drawLine(lineEndX,lineEndY,lineEndX+distance/2,lineEndY,linePaint);
            bitmapCanvas.drawText(desc,lineEndX+distance/2+4,lineEndY+rect.height()/2,textPaint);
        }else {
            bitmapCanvas.drawLine(lineEndX,lineEndY,lineEndX-distance/2,lineEndY,linePaint);
            bitmapCanvas.drawText(desc,lineEndX-distance/2-4-rect.width(),lineEndY+rect.height()/2,textPaint);
        }
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
        if(mode1 == MeasureSpec.EXACTLY){
            barHeight = size1;
        }else {
            if(datas != null && datas.size()>0){
                barHeight = (int) radius*2 + lengedHeight + (int) distance*2 + (int) distance*datas.size() ;
            }else {
                barHeight = minSize - getPaddingTop() - getPaddingBottom();
            }
        }

        return barHeight;
    }

    public void setOnSelectedListener(OnSelectedListener l){
        mListener = l;
    }

    public class AngleSE{
        private float startAngle;
        private float sweepAngle;

        public AngleSE(float startAngle, float sweepAngle) {
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public float getSweepAngle() {
            return sweepAngle;
        }
    }

    public interface OnSelectedListener{
        void onSelected(int position);
    }
}