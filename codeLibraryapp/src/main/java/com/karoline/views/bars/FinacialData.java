package com.karoline.views.bars;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by ${Karoline} on 2017/4/19.
 */

public class FinacialData {
    public String desc;
    public String targetV;
    public String finishedV;

    public FinacialData(String desc, String finishedV, String targetV) {
        this.desc = desc;
        this.finishedV = finishedV;
        this.targetV = targetV;
    }

    public float getRate(){
        if(targetV.contains("--")){
            return 0;
        }
        return Math.abs(Float.valueOf(finishedV) / Float.valueOf(targetV));
    }

    public String getDataDesc(){
        if(!TextUtils.isEmpty(finishedV) && !TextUtils.isEmpty(targetV)){
            Spanned spanned =  Html.fromHtml(finishedV +" / "  + "  <font color='#f57f17'>" + targetV + "</font> " +"(万元)") ;
            return spanned.toString();
        }

        return finishedV+"(万元)";
    }
}
