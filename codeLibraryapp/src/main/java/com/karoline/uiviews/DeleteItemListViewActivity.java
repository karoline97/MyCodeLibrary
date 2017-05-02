package com.karoline.uiviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.karoline.R;
import com.karoline.views.DeleteItemsListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeleteItemListViewActivity extends AppCompatActivity implements DeleteItemsListView.DeletesListener{
    @BindView(R.id.deleteitem_listview)
    DeleteItemsListView listView;
    @BindView(R.id.deleteitem_edit)
    TextView editView;

    private List<String> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item_list_view);
        ButterKnife.bind(this);

        listView.setDeletesListener(this);

        datas.add("测试1");
        datas.add("测试2");
        datas.add("测试3");
        datas.add("测试4");
        datas.add("测试5");
        datas.add("测试6");
        datas.add("测试7");
        datas.add("测试8");
        datas.add("测试9");
        datas.add("测试10");
        datas.add("测试11");
        datas.add("测试12");
        datas.add("测试13");
        datas.add("测试14");
        datas.add("测试15");
        datas.add("测试16");
        datas.add("测试17");
        datas.add("测试18");
        datas.add("测试19");
        datas.add("测试20");
        datas.add("测试21");
        datas.add("测试22");
        datas.add("测试23");
        datas.add("测试24");
        datas.add("测试25");
        datas.add("测试26");
        datas.add("测试27");
        datas.add("测试28");
        datas.add("测试29");
        datas.add("测试30");

        listView.setDatas(datas);

        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listView.isEditting()){
                    editView.setText("取消");
                    listView.showEditView();
                }else {
                    editView.setText("编辑");
                    listView.dismissEditView();
                }
            }
        });
    }

    @Override
    public void deleteDatas(List<String> dedatas) {
        datas.removeAll(dedatas);
        listView.setDatas(datas);
        editView.setText("编辑");
    }

    @Override
    public void noDatas() {
        editView.setText("编辑");
    }
}
