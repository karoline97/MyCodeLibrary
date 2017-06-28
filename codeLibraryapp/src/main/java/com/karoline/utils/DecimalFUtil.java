package com.karoline.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by ${Karoline} on 2017/5/11.
 */

public class DecimalFUtil {
    public static String formatTo3(double num){
        return new DecimalFormat(",###.00元").format(num);
    }

    public static String formatTo3(String price){
        if(TextUtils.isEmpty(price)){
            return "";
        }
        try {
            return new DecimalFormat(",###.00元").format(Double.valueOf(price));
        }catch (Exception e){
            return price;
        }
    }

    public static String formatToNormal(String price){
        if(TextUtils.isEmpty(price)){
            return "";
        }
        try {
            return new DecimalFormat(",###.00").format(Double.valueOf(price));
        }catch (Exception e){
            return "0";
        }
    }
}
