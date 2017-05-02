package com.karoline.uiviews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.karoline.R;
import com.karoline.adapter.MyViewPagerAdapter;
import com.karoline.fragment.SimpleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${Karoline} on 2016/9/2.
 */
public class CoodinatorLayoutActivity extends AppCompatActivity{

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CoordinatorLayout");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        setView();
    }

    private void setView(){

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(SimpleFragment.newInstance(0), "Tab1");//添加Fragment
        viewPagerAdapter.addFragment(SimpleFragment.newInstance(1), "Tab2");
        viewPagerAdapter.addFragment(SimpleFragment.newInstance(2), "Tab3");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器


        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置Tab可以滚动
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置Tab布满TabLayout(默认)
        tabLayout.setupWithViewPager(pager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(tabLayout,"fab上移",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
