package com.karoline.views.bars;

import com.karoline.utils.DecimalFUtil;

/**
 * Created by ${Karoline} on 2017/6/14.
 */

public class AssetKcData {
    private int color;
    private String desc;
    private Float num;

    public AssetKcData(int color, String desc, Float num) {
        this.color = color;
        this.desc = desc;
        this.num = num;
    }

    public int getColor() {
        return color;
    }

    public String getDesc() {
        return desc;
    }

    public Float getNum() {
        return num;
    }

    public String getSNum() {
        return DecimalFUtil.formatToNormal(num+"");
    }
}
