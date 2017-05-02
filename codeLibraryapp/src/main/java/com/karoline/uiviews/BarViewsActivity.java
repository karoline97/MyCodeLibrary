package com.karoline.uiviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.karoline.R;
import com.karoline.views.bars.AgeBar;
import com.karoline.beans.AgeData;
import com.karoline.beans.FinacialData;
import com.karoline.views.bars.FinancialBar;
import com.karoline.views.bars.NationBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarViewsActivity extends AppCompatActivity {

    @BindView(R.id.barviews_age)
    AgeBar ageBar;
    @BindView(R.id.barviews_horibar)
    FinancialBar horibar;
    @BindView(R.id.barviews_nation)
    NationBarView nationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_views);
        ButterKnife.bind(this);

        ageBar.setMaxValue(50);
        List<Integer> valus = new ArrayList<>();
        valus.add(10);
        valus.add(20);
        valus.add(30);
        valus.add(40);
        ageBar.setyValues(valus);

        List<AgeData> datas1 = new ArrayList<>();
        datas1.add(new AgeData("30及以下",2));
        datas1.add(new AgeData("30-35",8));
        datas1.add(new AgeData("35-40",27));
        datas1.add(new AgeData("40-50",24));
        datas1.add(new AgeData("50-55",19));
        datas1.add(new AgeData("55及以上",10));
        ageBar.setData(datas1);

        nationBar.setData(75,13,88);

        List<FinacialData> datas = new ArrayList<>();
        datas.add(new FinacialData("运营分公司","-305.6","-7701"));
        datas.add(new FinacialData("房地产公司","-30.5","79"));
        datas.add(new FinacialData("物业公司","-120.3","-105"));
        datas.add(new FinacialData("国展中心","-205.3","-556"));
        datas.add(new FinacialData("盛京通公司","25","--"));
        datas.add(new FinacialData("城市通公司","-150.6","--"));
        datas.add(new FinacialData("幼儿艺术学校","1.4","23"));
        datas.add(new FinacialData("建兴房产代理公司","-3","--"));
        datas.add(new FinacialData("地铁巴士公交公司","-703.2","--"));
        datas.add(new FinacialData("丰城巴士公司","90.5","--"));
        datas.add(new FinacialData("丰城公交公司","-52","--"));
        datas.add(new FinacialData("丰城汽服公司","-45.5","--"));
        datas.add(new FinacialData("盾构公司","-26.5","--"));
        datas.add(new FinacialData("经营公司","26.5","560"));
        horibar.setData(datas);
    }


}
