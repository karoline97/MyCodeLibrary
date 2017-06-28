package com.karoline.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.karoline.R;
import com.karoline.utils.SizeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${Karoline} on 2017/4/26.
 */

public class TimeSelectDialog {
    private Context mContext;
    private AlertDialog  mDialog;
    private Calendar mCalendar;
    private TimeSlectedListener mListener;
    private View dialogContent;

    public TimeSelectDialog(Context context, TimeSlectedListener listener){
        mContext = context;
        mListener = listener;

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());

        dialogContent = getDialogView();
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.setCanceledOnTouchOutside(false);
    }

    private View getDialogView(){
        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(params0);
        container.setBackgroundColor(Color.WHITE);

        TextView titleText = new TextView(mContext);
        titleText.setText("选择时间");
        titleText.setBackgroundResource(R.color.colorPrimary);
        titleText.setTextColor(Color.WHITE);
        titleText.setTextSize(18);//(SizeUtils.sp2px(mContext,18));
        titleText.setGravity(Gravity.CENTER);
        titleText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) SizeUtils.dp2px(mContext,56f)));

        LinearLayout timeSelect = new LinearLayout(mContext);
        timeSelect.setOrientation(LinearLayout.HORIZONTAL);
        timeSelect.setLayoutParams(params0);
        timeSelect.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT,1);
        params1.setMargins((int) SizeUtils.dp2px(mContext,8),0,(int) SizeUtils.dp2px(mContext,8),0);

        final NumberPicker mYear = new NumberPicker(mContext);
        mYear.setLayoutParams(params1);
        mYear.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker mMonth = new NumberPicker(mContext);
        mMonth.setLayoutParams(params1);
        mMonth.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker mDay = new NumberPicker(mContext);
        mDay.setLayoutParams(params1);
        mDay.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        /*final NumberPicker mHour = new NumberPicker(mContext);
        mHour.setLayoutParams(params1);
        mHour.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker mMinute = new NumberPicker(mContext);
        mMinute.setLayoutParams(params1);
        mMinute.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);*/

        timeSelect.removeAllViews();
        timeSelect.addView(mYear);
        timeSelect.addView(mMonth);
        timeSelect.addView(mDay);
       // timeSelect.addView(mHour);
       // timeSelect.addView(mMinute);

        TextView divider= new TextView(mContext);
        divider.setBackgroundColor(Color.LTGRAY);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) SizeUtils.dp2px(mContext,1));
        params3.setMargins(0,0,0,(int) SizeUtils.dp2px(mContext,8));
        divider.setLayoutParams(params3);

        LinearLayout selectBtn = new LinearLayout(mContext);
        selectBtn.setOrientation(LinearLayout.HORIZONTAL);
        selectBtn.setLayoutParams(params0);
        selectBtn.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,
                (int) SizeUtils.dp2px(mContext,48),1);
        params2.setMargins((int)SizeUtils.dp2px(mContext,8),0,
                (int)SizeUtils.dp2px(mContext,8),(int) SizeUtils.dp2px(mContext,8));
        TextView cancelText = new TextView(mContext);
        cancelText.setText("取消");
        cancelText.setBackgroundResource(R.color.colorPrimary);
        cancelText.setTextColor(Color.WHITE);
        cancelText.setTextSize(14);//(SizeUtils.sp2px(mContext,14));
        cancelText.setGravity(Gravity.CENTER);
        cancelText.setLayoutParams(params2);

        TextView okText = new TextView(mContext);
        okText.setText("确定");
        okText.setBackgroundResource(R.color.colorPrimary);
        okText.setTextColor(Color.WHITE);
        okText.setTextSize(14);//(SizeUtils.sp2px(mContext,14));
        okText.setGravity(Gravity.CENTER);
        okText.setLayoutParams(params2);
        selectBtn.removeAllViews();
        selectBtn.addView(cancelText);
        selectBtn.addView(okText);

        container.removeAllViews();
        container.addView(titleText);
        container.addView(timeSelect);
        container.addView(divider);
        container.addView(selectBtn);

        mYear.setMinValue(2000);
        mYear.setMaxValue(2116);
        mYear.setValue(mCalendar.get(Calendar.YEAR));

        mMonth.setMaxValue(12);
        mMonth.setMinValue(1);
        mMonth.setValue(mCalendar.get(Calendar.MONTH)+1);

        mDay.setMaxValue(getMonthLastDay());
        mDay.setMinValue(getMonthFirstDay());
        mCalendar.setTime(new Date());
        mDay.setValue(mCalendar.get(Calendar.DATE));

        /*mHour.setMaxValue(23);
        mHour.setMinValue(0);
        mHour.setValue(mCalendar.get(Calendar.HOUR_OF_DAY));

        mMinute.setMaxValue(59);
        mMinute.setMinValue(0);
        mMinute.setValue(mCalendar.get(Calendar.MINUTE));*/

        mYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay.setMaxValue(getMonthLastDay());
                mDay.setMinValue(getMonthFirstDay());
                mDay.setValue(getMonthFirstDay());
            }
        });

        mMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCalendar.set(Calendar.MONTH,newVal);
                mDay.setMaxValue(getMonthLastDay());
                mDay.setMinValue(getMonthFirstDay());
                mDay.setValue(getMonthFirstDay());
            }
        });

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.set(mYear.getValue(),mMonth.getValue()-1,mDay.getValue(),0,0
                        );//mHour.getValue(),mMinute.getValue());

                mListener.seletedTime(mCalendar.getTimeInMillis());
                mDialog.dismiss();
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        return container;
    }

    public void showTimeSelectDialog(){
        if(mDialog != null && !mDialog.isShowing()){
            mDialog.show();
            Window window = mDialog.getWindow();
            window.setContentView(dialogContent);
        }
    }

    private int getMonthFirstDay(){
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.CHINA);
        return Integer.valueOf(format.format(mCalendar.getTime()));
    }

    private int getMonthLastDay(){
        mCalendar.set(Calendar.DAY_OF_MONTH,
                mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat format = new SimpleDateFormat("dd",Locale.CHINA);
        return Integer.valueOf(format.format(mCalendar.getTime()));
    }

    public interface TimeSlectedListener{
        void seletedTime(long timeInMillis);
    }
}
