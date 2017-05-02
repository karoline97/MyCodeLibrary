package com.karoline.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karoline.R;
import com.karoline.views.SimpleRecyclerListCell;

import java.util.List;

/**
 * Created by ${Karoline} on 2016/9/8.
 */
public class recyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerListCell>{
    private List<String> mData;

    public void setData(List<String> datas){
        this.mData = datas;
    }

    @Override
    public SimpleRecyclerListCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_recyclerview_item,null,false);
        return new SimpleRecyclerListCell(view);
    }

    @Override
    public void onBindViewHolder(SimpleRecyclerListCell holder, int position) {
                holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
