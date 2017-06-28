package com.karoline.views.bars;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/3/17.
 */

public class PieChartWithLine {
    private PieChart mChart;
    private String centerText;
    private Context mCotext;

    public PieChartWithLine(Context context, PieChart chart, String centerText){
        this.mCotext = context;
        this.mChart = chart;
        this.centerText = centerText;
        initChart();
    }

    private void initChart(){
        mChart.setHoleRadius(54f);  //半径
        mChart.setTransparentCircleRadius(58f); // 半透明圈
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(0, 5, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

       // mChart.setDescriptionColor(Color.BLACK);
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(14f);
        //mChart.setNoDataTextColor(Color.BLACK);
        mChart.setUsePercentValues(true);  //显示成百分比

        mChart.setCenterText(centerText);
        mChart.setCenterTextSize(16f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(-90);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        //mChart.animateXY(1000,1000); //设置动画

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);

        Legend mLegend = mChart.getLegend();  //设置比例图
       // mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
        mLegend.setForm(Legend.LegendForm.SQUARE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
    }

    public  void setData(List<Integer> perNums, List<String> perDescs,
                         List<Integer> colors, int totalNum) {
        if(mChart == null ){
            Log.d("PieChartWithLine","图表对象为空");
            return;
        }
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (int i = 0; i < perDescs.size(); i++) {
            entries.add(new PieEntry((float)perNums.get(i)/totalNum * 100, perDescs.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(0f);//设置饼图部分间隙
        dataSet.setSelectionShift(8f);

        //颜色：rgb值Color.rgb(51, 181, 229);
        dataSet.setColors(colors);

        dataSet.setValueTextColor(Color.GRAY);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(1.0f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineVariableLength(true);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);
        //data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();

    }

}
