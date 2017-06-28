package com.karoline.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by ${Karoline} on 2017/5/2.
 */

public class SwipRefreshLayoutInViewPager extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    //private View view;
    private ListView mListView;
    private int mTouchSlop;
    // 上一次触摸时的X坐标
    private float mPrevX;
    private float mYDown;
    private float mLastY;
    private boolean isTop = false;
    private int visibleItemcount = 0;


    public SwipRefreshLayoutInViewPager(Context context) {
        super(context);
    }

    public SwipRefreshLayoutInViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                // Log.d("refresh" ,"move----" + eventX + "   " + mPrevX + "   " + mTouchSlop);
                // 增加60的容差，让下拉刷新在竖直滑动时可以触发
                if (xDiff > mTouchSlop + 60) {
                    return false;
                }

        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if(visibleItemcount ==0){
                    this.setEnabled(true);
                }else if (canRefresh()) {
                    this.setEnabled(true);
                } else {
                    this.setEnabled(false);
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean isPullDown() {
        return (mLastY - mYDown) >= mTouchSlop;
    }

    private boolean setIsTop(boolean top) {
        this.isTop = top;
        return isTop;
    }

    private boolean canRefresh() {
        return isPullDown() && isTop;
    }

    /**
     * 获取ListView对象
     */
    public void setListView(ListView listView) {
        mListView = listView;
        mListView.setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        visibleItemcount = visibleItemCount;

        if (firstVisibleItem == 0) {
            View firstVisibleItemView = mListView.getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                Log.d("ListView", "##### 滚动到顶部 #####");
                setIsTop(true);
                if(canRefresh()){
                    this.setEnabled(true);
                }
            }
        } /*else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            View lastVisibleItemView = mListView.getChildAt(mListView.getChildCount() - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == mListView.getHeight()) {
                Log.d("ListView", "##### 滚动到底部 ######");
            }
        }*/else{
            setIsTop(false);
            this.setEnabled(false);
        }
    }
}
