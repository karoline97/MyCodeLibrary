package com.karoline.views.bars;

/**
 * Created by ${Karoline} on 2017/5/22.
 */

public class CsjdData {
    private String name;//各处室名称（安监处，总工处）
    private String contractAmont;//合同总金额（对应蓝色线条 10亿元）
    private String ImplementSituation;//执行情况（对应红色线条 20

    public float getTotal() {
        return Float.valueOf(contractAmont);
    }

    public int getMax() {
        return Integer.valueOf(contractAmont);
    }

    public String getName() {
        return name;
    }

    public float getDone() {
        return Float.valueOf(ImplementSituation);
    }

    public CsjdData(String contractAmont, String implementSituation, String name) {
        this.contractAmont = contractAmont;
        ImplementSituation = implementSituation;
        this.name = name;
    }
}
