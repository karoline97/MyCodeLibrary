package com.karoline.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karoline.R;
import com.karoline.adapter.recyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Karoline} on 2016/9/9.
 */
public class SimpleFragment extends Fragment{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private int type;

    List<String> temList = new ArrayList<String>();


    public SimpleFragment() {
        for(int i=0;i<50;i++){
            temList.add("item"+i);
        }
    }

    public static SimpleFragment newInstance(int index) {
        SimpleFragment fragment = new SimpleFragment();
        Bundle args = new Bundle();
        args.putInt("type", index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savednstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.recyclerview, container, false);
       recyclerView = (RecyclerView) ret.findViewById(R.id.recyclelist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter adapter= new recyclerAdapter();
        adapter.setData(temList);
        recyclerView.setAdapter(adapter);
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
