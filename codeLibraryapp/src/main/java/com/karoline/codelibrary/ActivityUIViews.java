package com.karoline.codelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.karoline.R;
import com.karoline.uiviews.AutoCompleteEditTextActivity;
import com.karoline.uiviews.BarViewsActivity;
import com.karoline.uiviews.CoodinatorLayoutActivity;
import com.karoline.uiviews.CoordinatorCollapsingActivity;
import com.karoline.uiviews.DeleteItemListViewActivity;
import com.karoline.uiviews.DrawerLayoutActivity;
import com.karoline.uiviews.TimeSelectActivity;

import butterknife.BindView;
import common.versionupdate.CheckUpdateTask;

/**
 * Created by ${Karoline} on 2016/9/2.
 */
public class ActivityUIViews extends BaseToolBarActivity implements View.OnClickListener{
    @BindView(R.id.uiviews_coordinatorlayoutandtollbar)
    View coordinatorlayoutandtollbar;
    @BindView(R.id.uiviews_cactlayout)
    View cactlayout;
    @BindView(R.id.uiviews_drawerlayout_navigationview)
    View drawLayout;
    @BindView(R.id.uiviews_complatetextview)
    View complatetextview;
    @BindView(R.id.uiviews_barview)
    View barView;
    @BindView(R.id.uiviews_deleteitem)
    View deleteItem;
    @BindView(R.id.uiviews_timeselect)
    View timeSelect;

    public final static String ACCOUNT_SID = "5145e0db47051d35b69a9da9945148612f9be95f";
    public final static String AUTO_TOKEN = "b8e1fa5fc6f80bae9f64cdf34df950deb29511f8";
    public final static String APP_CODE = "658c9d082ca57b2eb5c23ad453d08b9b72483d25";

    @Override
    int getContentView() {
        return R.layout.activity_ui_views;
    }

    @Override
    void init(Bundle savedInstanceState) {

        setTitle("ActivityUIViews");
        setBackVisible();

        setRightBtn1Visible(R.drawable.ball_red, new onBtnClickListner() {
            @Override
            public void onBtnClick() {
                Snackbar.make(timeSelect,"你点击右边第一个按钮",Snackbar.LENGTH_SHORT).show();
            }
        });

        /*setRightBtn2Visible(R.drawable.ball_red, new onBtnClickListner() {
            @Override
            public void onBtnClick() {
                Snackbar.make(timeSelect,"你点击右边第二个按钮",Snackbar.LENGTH_SHORT).show();
            }
        });*/

        coordinatorlayoutandtollbar.setOnClickListener(this);
        cactlayout.setOnClickListener(this);
        drawLayout.setOnClickListener(this);
        complatetextview.setOnClickListener(this);
        barView.setOnClickListener(this);
        deleteItem.setOnClickListener(this);
        timeSelect.setOnClickListener(this);

        CheckUpdateTask.setAccountInfo(ACCOUNT_SID,AUTO_TOKEN,APP_CODE);
        CheckUpdateTask task = new CheckUpdateTask(this, false, new CheckUpdateTask.UpdateListener() {
            @Override
            public void VersionUpdateFail(String msg) {
                Snackbar.make(cactlayout,msg,Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void noUpdate() {

            }
        });
       // task.execute("android","prod");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uiviews_coordinatorlayoutandtollbar:
                enterActivity(CoodinatorLayoutActivity.class);
                break;
            case R.id.uiviews_cactlayout:
                Intent intent1 = new Intent(ActivityUIViews.this,CoordinatorCollapsingActivity.class);
                intent1.putExtra("layoutid",R.layout.coordinator_collapsingtoolbar);
                startActivity(intent1);
                break;
            case R.id.uiviews_drawerlayout_navigationview:
                enterActivity(DrawerLayoutActivity.class);
                break;

            case R.id.uiviews_complatetextview:
                enterActivity(AutoCompleteEditTextActivity.class);
                break;

            case R.id.uiviews_barview:
                enterActivity(BarViewsActivity.class);
                break;

            case R.id.uiviews_deleteitem:
                enterActivity(DeleteItemListViewActivity.class);
                break;

            case R.id.uiviews_timeselect:
                enterActivity(TimeSelectActivity.class);
                break;

            default:
                break;
        }
    }

}
