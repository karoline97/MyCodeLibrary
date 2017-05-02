package com.karoline.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.karoline.R;
import com.karoline.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyAnimationActivity extends AppCompatActivity {
    @BindView(R.id.propertyanimation_image)
    ImageView imageView;

    @BindView(R.id.propertyanimation_ball)
    ImageView ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("属性动画");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.propertyanimation_image)
    protected void onImageClick(View v){
        rotateyAnimRun(v);
        propertyValuesHolder(v);
    }

    /*public void rotateyAnimRun(View view)
    {
        ObjectAnimator
                .ofFloat(view, "rotationX", 0.0F, 360.0F)
                .setDuration(500)//
                .start();
    }*/

    public void rotateyAnimRun(final View view)
    {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "zhy", 1.0F,  0.0F)//
                .setDuration(500);//
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }

    //使用多种动画效果
    public void propertyValuesHolder(View view)
    {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                0, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY,pvhZ).setDuration(1000).start();
    }

    //垂直移动,自由落体
    public void verticalRun(final View view)
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0, ScreenUtils.getScreenHeight(this)-ball.getHeight());
        animator.setTarget(view);
        animator.setDuration(1000).start();
        //      animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                ball.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    /**
     * 抛物线
     * @param mBlueBall
     */
    public void paowuxian(final View mBlueBall)
    {

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>()
        {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue)
            {
                //Log.e(TAG, fraction * 3 + "");
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                PointF point = (PointF) animation.getAnimatedValue();
                ball.setX(point.x);
                ball.setY(point.y);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
