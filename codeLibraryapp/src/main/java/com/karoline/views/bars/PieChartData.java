package com.karoline.views.bars;

import java.util.List;

/**
 * Created by ${Karoline} on 2017/5/11.
 */

public class PieChartData {
    private List<Integer> perNums;
    private List<String> perDescs;
    private  List<Integer> colors;
    private int totalNum;

    public PieChartData(List<Integer> perNums, List<String> perDescs,
                        List<Integer> colors, int totalNum) {
        this.colors = colors;
        this.perDescs = perDescs;
        this.perNums = perNums;
        this.totalNum = totalNum;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public List<String> getPerDescs() {
        return perDescs;
    }

    public List<Integer> getPerNums() {
        return perNums;
    }

    public int getTotalNum() {
        return totalNum;
    }
}
