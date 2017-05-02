package com.karoline.uiviews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.karoline.R;
import com.karoline.views.AutoComplateEditTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoCompleteEditTextActivity extends AppCompatActivity implements AutoComplateEditTextView.ClickItemAndTextChangedLisener<AutoCompleteEditTextActivity.TestData> {
    @BindView(R.id.edit_text)
    AutoComplateEditTextView editTextView;

    private String text;
    private List<TestData> datas = new ArrayList<>();

    private ProgressDialog dialog;
    private Handler mHandler;
    private final int ID_REQUEST_START = 1;
    private final int ID_REQUEST_END= 2;
    private List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_edit_text);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == ID_REQUEST_START){
                    dialog.show();
                }else if(msg.what == ID_REQUEST_END){
                    onResponse(datas);
                }
            }
        };
        editTextView.setClickItemAndTextChangedLisener(this);
        editTextView.setHandlerandRequest(mHandler,runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));
            datas.add(new TestData("123","测试数据"));

            mHandler.obtainMessage(ID_REQUEST_START).sendToTarget();
            mHandler.sendEmptyMessageDelayed(ID_REQUEST_END,2000);
        }
    };

    private void onResponse(List<TestData> strings){
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        stringList.clear();
        for(TestData testData : strings){
            stringList.add(testData.name);

        }
        editTextView.setDatas(strings,stringList);
    }

    @Override
    public void onClickItem(TestData data) {
        editTextView.setText(data.name);
        editTextView.setSelection(data.name.length());
    }

    @Override
    public void textChanged(String text) {
        this.text = text;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_DEL) {
                editTextView.dissDrop();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class TestData{
        public String id;
        public String name;

        public TestData(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
