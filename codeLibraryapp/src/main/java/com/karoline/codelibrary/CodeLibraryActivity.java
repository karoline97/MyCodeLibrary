package com.karoline.codelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.karoline.R;
import com.karoline.animation.PropertyAnimationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CodeLibraryActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.my_chartExample)
    TextView myChart;
    @BindView(R.id.my_uiviews)
    View uiViews;
    @BindView(R.id.my_testRxandroid)
    View rxandroid;
    @BindView(R.id.my_retrofit)
    View retrofit;
    @BindView(R.id.my_animation)
    View animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_library);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("工具库");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        setOnClickListener();

    }

    private void setOnClickListener(){
        myChart.setOnClickListener(this);
        uiViews.setOnClickListener(this);
        rxandroid.setOnClickListener(this);
        retrofit.setOnClickListener(this);
        animation.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_chartExample:
                startActivity(new Intent(CodeLibraryActivity.this,
                        com.xxmassdeveloper.mpchartexample.notimportant.MainActivity.class));
                break;
            case R.id.my_uiviews:
                startActivity(new Intent(CodeLibraryActivity.this,ActivityUIViews.class));
                break;
            case R.id.my_testRxandroid:
                startActivity(new Intent(CodeLibraryActivity.this,TestRxAndroidActivity.class));
                break;
            case R.id.my_animation:
                startActivity(new Intent(CodeLibraryActivity.this,PropertyAnimationActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_code_library, menu);
        return true;
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
