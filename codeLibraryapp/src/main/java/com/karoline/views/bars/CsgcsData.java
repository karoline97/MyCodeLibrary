package com.karoline.views.bars;

/**
 * Created by ${Karoline} on 2017/5/11.
 */

public class CsgcsData {
    private String desc;
    private double count;

    public CsgcsData(String desc,double count) {
        this.count = count;
        this.desc = desc;
    }

    public double getCount() {
        return count;
    }

    public String getDesc() {
        return desc;
    }
}
