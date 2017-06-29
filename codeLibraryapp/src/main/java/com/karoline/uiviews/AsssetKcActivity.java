package com.karoline.uiviews;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.karoline.R;
import com.karoline.codelibrary.BaseToolBarActivity;
import com.karoline.views.bars.AssetKcData;
import com.karoline.views.bars.AssetKcPie;
import com.karoline.views.bars.AssetLenged;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AsssetKcActivity extends BaseToolBarActivity implements AssetKcPie.OnSelectedListener {
    @BindView(R.id.assetkc_desc)
    TextView assetkcDesc;
    @BindView(R.id.assetkc_chart)
    AssetKcPie assetkcPie;
    @BindView(R.id.assetkc_desc1)
    TextView assetkcDesc1;
    @BindView(R.id.assetkc_chart1)
    AssetKcPie assetkcChart1;
    @BindView(R.id.assetkc_lenged)
    AssetLenged assetkcLenged;

    private int mode = 1;
    private List<Integer> perColorList;
    private List<String> descList = new ArrayList<>();
    private List<AssetKcData> kcDatas;


    @Override
    protected int getContentView() {
        return R.layout.activity_assset_kc;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("库存概览");
        assetkcPie.setOnSelectedListener(this);
        assetkcPie.setLenged();

        assetkcDesc1.setText("");

        kcDatas = new ArrayList<>();
        perColorList = new ArrayList<>();
        perColorList.add(Color.parseColor("#f36c60"));
        perColorList.add(Color.parseColor("#fba6c8"));
        perColorList.add(Color.parseColor("#7986cb"));
        perColorList.add(Color.parseColor("#4fc3f7"));
        perColorList.add(Color.parseColor("#cfd8dc"));
        perColorList.add(Color.parseColor("#4db6ac"));
        perColorList.add(Color.parseColor("#aed581"));
        perColorList.add(Color.parseColor("#fff176"));
        perColorList.add(Color.parseColor("#ffb74d"));


        updateView();
    }


    private void updateView() {
        List<AssetKcData> list = new ArrayList<>();
        list.add(new AssetKcData(Color.rgb(205, 92, 92), "工器具", 318243f));
        list.add(new AssetKcData(Color.rgb(255, 193, 37), "消耗品", 674937f));
        list.add(new AssetKcData(Color.parseColor("#cfd8dc"), "设施设备工装", 212664f));
        list.add(new AssetKcData(Color.rgb(10, 149, 237), "原材料", 35420464f));
        assetkcPie.setData(list, 318243 + 674937 + 212664 + 35420464);
    }

    @Override
    public void onSelected(int position) {
        List<AssetKcData> list = new ArrayList<>();
        switch (position) {
            case 0:
                assetkcDesc1.setText("工器具金额");
                assetkcChart1.setVisibility(View.VISIBLE);
                list.clear();
                list.add(new AssetKcData(perColorList.get(0), "电动工具", 38189.16f));
                list.add(new AssetKcData(perColorList.get(1), "手动工具", 25459.44f));
                list.add(new AssetKcData(perColorList.get(2), "仪器仪表", 12729.72f));
                list.add(new AssetKcData(perColorList.get(3), "照明灯具", 12729.72f));
                list.add(new AssetKcData(perColorList.get(4), "通讯工具", 25459.44f));
                list.add(new AssetKcData(perColorList.get(5), "杂项工具", 12729.72f));

                descList.clear();
                descList.add("电动工具");
                descList.add("手动工具");
                descList.add("仪器仪表");
                descList.add("照明灯具");
                descList.add("通讯工具");
                descList.add("杂项工具");

                assetkcChart1.setData(list, 127297.20f);
                assetkcLenged.setData(perColorList, descList);
                break;
            case 1:
                assetkcDesc1.setText("消耗品金额");
                assetkcChart1.setVisibility(View.VISIBLE);
                list.clear();
                list.add(new AssetKcData(perColorList.get(0), "刀具", 134987.4f));
                list.add(new AssetKcData(perColorList.get(1), "防寒防汛", 53994.96f));
                list.add(new AssetKcData(perColorList.get(2), "劳防用品", 80992.44f));

                descList.clear();
                descList.add("刀具");
                descList.add("防寒防汛");
                descList.add("劳防用品");

                assetkcChart1.setData(list, 269974.8f);
                assetkcLenged.setData(perColorList, descList);
                break;
            case 2:
                assetkcDesc1.setText("设施设备工装金额");
                assetkcChart1.setData(null, 100f);
                assetkcLenged.setData(null, null);
                break;
            case 3:
                assetkcDesc1.setText("原材料金额");
                assetkcChart1.setVisibility(View.VISIBLE);
                list.clear();
                list.add(new AssetKcData(perColorList.get(0), "备品备件（专用)", 1416818.56f));
                list.add(new AssetKcData(perColorList.get(1), "备品备件（通用）", 1558500.416f));
                list.add(new AssetKcData(perColorList.get(2), "紧固件", 4250455.68f));
                list.add(new AssetKcData(perColorList.get(3), "水暖配件", 708409.28f));
                list.add(new AssetKcData(perColorList.get(4), "电器电料", 2833637.12f));
                list.add(new AssetKcData(perColorList.get(5), "灯类", 2125227.84f));
                list.add(new AssetKcData(perColorList.get(6), "辅料", 566727.424f));
                list.add(new AssetKcData(perColorList.get(7), "材料", 283363.712f));
                list.add(new AssetKcData(perColorList.get(8), "化工原料", 425045.568f));

                descList.clear();
                descList.add("备品备件（专用)");
                descList.add("备品备件（通用）");
                descList.add("紧固件");
                descList.add("水暖配件");
                descList.add("电器电料");
                descList.add("灯类");
                descList.add("辅料");
                descList.add("材料");
                descList.add("化工原料");

                assetkcChart1.setData(list, 14168185.6f);
                assetkcLenged.setData(perColorList, descList);
                break;
        }
    }
}
