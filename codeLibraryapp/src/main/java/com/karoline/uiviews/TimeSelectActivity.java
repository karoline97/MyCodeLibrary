package com.karoline.uiviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.karoline.R;
import com.karoline.utils.TimeUtils;
import com.karoline.views.AddAndSubView;
import com.karoline.views.TimeSelectDialog;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeSelectActivity extends AppCompatActivity implements AddAndSubView.OnNumChangeListener{
    @BindView(R.id.timeselect_text)
    TextView textView;
    @BindView(R.id.timeselect_addandsub)
    AddAndSubView andSubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_select);
        ButterKnife.bind(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectDialog dialog = new TimeSelectDialog(TimeSelectActivity.this, new TimeSelectDialog.TimeSlectedListener() {
                    @Override
                    public void seletedTime(long timeInMillis) {
                        textView.setText(TimeUtils.milliseconds2String(timeInMillis, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
                    }
                });
                dialog.showTimeSelectDialog();
            }
        });
        andSubView.setOnNumChangeListener(this);
    }

    @Override
    public void onNumChange(View view, int num) {
        textView.setText(num+"");
    }
}
