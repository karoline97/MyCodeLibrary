package com.karoline.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.china317.syrailway.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/6/28.
 */
public class LoopViewPager extends FrameLayout {

    private  boolean isAutoPlay = false;
    private List<SubsamplingScaleImageView> imageViews;
    private List<ImageView> dotImageViews;
    private LinearLayout dotLinearLayout;

    private ViewPager mViewpager;
    private int mCurrentItem = 0;
    private ScheduledExecutorService service;
    private ImageCycleViewListener mImageCycleViewListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewpager.setCurrentItem(mCurrentItem);
        }
    };


    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initUI(context);

        if (isAutoPlay) {
            startPlay();
        }
    }

    public void setIsAutoPlay(Boolean autoPlay){
        this.isAutoPlay = autoPlay;
    }

    private void initUI(Context context) {

        imageViews = new ArrayList<>();
        dotImageViews = new ArrayList<>();

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        dotLinearLayout = (LinearLayout) findViewById(R.id.layout_dot);
        mViewpager = (ViewPager) findViewById(R.id.viewPager);

    }

    public void setImages(List<Bitmap> images, ImageCycleViewListener listener) {
        mImageCycleViewListener = listener;
        imageViews.clear();
        dotImageViews.clear();
        dotLinearLayout.removeAllViews();

        for(int i = 0;i<images.size();i++){
            SubsamplingScaleImageView  imageView = new SubsamplingScaleImageView (getContext());
            imageView.setImage(ImageSource.bitmap(images.get(i)));
            imageViews.add(imageView);

        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 0, 0, 0);

        for (int i = 0; i < imageViews.size(); i++) {

            ImageView dotImageView = new ImageView(getContext());
            if (i == 0) {
                dotImageView.setBackgroundResource(R.drawable.icon_point_pre);
            } else {
                dotImageView.setBackgroundResource(R.drawable.icon_point);
            }

            dotImageView.setLayoutParams(lp);
            dotImageViews.add(dotImageView);
            dotLinearLayout.addView(dotImageView);

        }

        mViewpager.setFocusable(true);
        mViewpager.setAdapter(new MyPagerAdapter());
        mViewpager.setOnPageChangeListener(new MyPageChangeListener());
    }


    public void startPlay() {
        if(!isAutoPlay) return;
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new SlideShowTask(), 2, 5, TimeUnit.SECONDS);
    }

    private void stopPlay() {
        service.shutdown();
    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            SubsamplingScaleImageView  v = imageViews.get(position);
            if (mImageCycleViewListener != null) {
                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mImageCycleViewListener.onImageClick(position, v);
                    }
                });
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }
    }


    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setImageBackground(position % imageViews.size());
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (mViewpager.getCurrentItem() == mViewpager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mViewpager.setCurrentItem(0);
                    } else if (mViewpager.getCurrentItem() == 0 && !isAutoPlay) {
                        mViewpager.setCurrentItem(mViewpager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }
    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < dotImageViews.size(); i++) {
            if (i == selectItems) {
                dotImageViews.get(i).setBackgroundResource(R.drawable.icon_point_pre);
            } else {
                dotImageViews.get(i).setBackgroundResource(R.drawable.icon_point);
            }
        }
    }

    public void destoryBitmaps() {
/*
        for (int i = 0; i < imageViews.size(); i++) {
            SubsamplingScaleImageView  imageView = imageViews.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {

                drawable.setCallback(null);
            }
        }*/
    }


    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (mViewpager) {
                mCurrentItem = (mCurrentItem + 1) % imageViews.size();
                mHandler.obtainMessage().sendToTarget();
            }
        }
    }


    public interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param //position
         * @param imageView
         */
        void onImageClick(int postion, View imageView);
    }

}
