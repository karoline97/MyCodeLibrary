
package com.xxmassdeveloper.mpchartexample;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.xxmassdeveloper.mpchartexample.custom.DayAxisValueFormatter;
import com.xxmassdeveloper.mpchartexample.custom.MyAxisValueFormatter;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;

public class BarChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    protected BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);

        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);

        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new DayAxisValueFormatter(mChart));

        AxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
        l.setForm(LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        setData(12, 50);

        // setting data
        mSeekBarY.setProgress(50);
        mSeekBarX.setProgress(12);

        mSeekBarY.setOnSeekBarChangeListener(this);
        mSeekBarX.setOnSeekBarChangeListener(this);

        // mChart.setDrawLegend(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.actionToggleValues) {
            for (IDataSet set : mChart.getData().getDataSets())
                set.setDrawValues(!set.isDrawValuesEnabled());
            mChart.invalidate();
        } else if (i == R.id.actionToggleHighlight) {
            if (mChart.getData() != null) {
                mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                mChart.invalidate();
            }
        } else if (i == R.id.actionTogglePinch) {
            if (mChart.isPinchZoomEnabled())
                mChart.setPinchZoom(false);
            else
                mChart.setPinchZoom(true);
            mChart.invalidate();
        } else if (i == R.id.actionToggleAutoScaleMinMax) {
            mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
            mChart.notifyDataSetChanged();
        } else if (i == R.id.actionToggleBarBorders) {
            for (IBarDataSet set : mChart.getData().getDataSets())
                ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);
            mChart.invalidate();
        } else if (i == R.id.animateX) {
            mChart.animateX(3000);
        } else if (i == R.id.animateY) {
            mChart.animateY(3000);
        } else if (i == R.id.animateXY) {
            mChart.animateXY(3000, 3000);
        } else if (i == R.id.actionSave) {
            if (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                        Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                        .show();
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress() + 2));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress() + 1 , mSeekBarY.getProgress());
        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    private void setData(int count, float range) {

        float start = 0f;

        mChart.getXAxis().setAxisMinValue(start);
        mChart.getXAxis().setAxisMaxValue(start + count + 2);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count + 1; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(i + 1f, val));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();
    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }
}
