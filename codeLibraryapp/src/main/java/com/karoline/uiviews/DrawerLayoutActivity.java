package com.karoline.uiviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.karoline.R;
import com.karoline.codelibrary.CodeLibraryActivity;

/**
 * Created by ${Karoline} on 2016/9/7.
 */
public class DrawerLayoutActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    @Override
    protected void onNewIntent(Intent intent) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DrawerLayoutActivity");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //设置NavigationView点击事件
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);
    }

    //设置NavigationView点击事件
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_example:
                                startActivity(new Intent(DrawerLayoutActivity.this, CoodinatorLayoutActivity.class));
                                break;
                            case R.id.navigation_item_blog:
                                startActivity(new Intent(DrawerLayoutActivity.this, CodeLibraryActivity.class));
                                break;
                            case R.id.navigation_item_about:
                                startActivity(new Intent(DrawerLayoutActivity.this, CoodinatorLayoutActivity.class));
                                break;

                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
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
