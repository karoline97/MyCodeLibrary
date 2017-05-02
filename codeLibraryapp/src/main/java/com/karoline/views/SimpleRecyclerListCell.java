package com.karoline.views;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.karoline.R;

/**
 * Created by ${Karoline} on 2016/9/8.
 */
public class SimpleRecyclerListCell extends RecyclerView.ViewHolder{
    AppCompatTextView textView;

    public SimpleRecyclerListCell(View itemView) {
        super(itemView);
        textView  = (AppCompatTextView)itemView.findViewById(R.id.simple_text);
    }

    public void setData(String datas){

        textView.setText(datas);
    }
}
