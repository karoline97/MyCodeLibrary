package com.karoline.codelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.karoline.R;

import butterknife.ButterKnife;

public abstract class BaseToolBarActivity extends AppCompatActivity {
    private TextView titleText;
    private Toolbar toolbar;
    private ImageView leftBtn;
    private ImageView rightBtn1;
    private ImageView rightBtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_tool_bar);

        titleText = (TextView)findViewById(R.id.base_title);
        leftBtn = (ImageView)findViewById(R.id.base_leftbutton0);
        rightBtn1 = (ImageView)findViewById(R.id.base_rightbutton1);
        rightBtn2 = (ImageView)findViewById(R.id.base_rightbutton2);

        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //不显示自带的title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        FrameLayout layout = (FrameLayout)findViewById(R.id.base_viewcontainer);
        LayoutInflater.from(this).inflate(getContentView(),layout);
        ButterKnife.bind(this);

        init(savedInstanceState);
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            titleText.setText(title);
        }
    }

    public void setBackVisible(){
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setImageResource(R.mipmap.goback);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setRightBtn1Visible(int iconID, final onBtnClickListner listner){
        rightBtn1.setVisibility(View.VISIBLE);
        rightBtn1.setImageResource(iconID);
        rightBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onBtnClick();
            }
        });

    }

    public void setRightBtn2Visible(int iconID, final onBtnClickListner listner){
        rightBtn2.setVisibility(View.VISIBLE);
        rightBtn2.setImageResource(iconID);
        rightBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onBtnClick();
            }
        });

    }

    public void enterActivity(Class classe){
        Intent intent = new Intent(this,classe);
        startActivity(intent);
    }

    public void enterActivityFinish(Class classe){
        Intent intent = new Intent(this,classe);
        startActivity(intent);
        finish();
    }

    protected abstract int getContentView();
    protected abstract void init(Bundle savedInstanceState);

    public interface onBtnClickListner{
        void onBtnClick();
    }

}
