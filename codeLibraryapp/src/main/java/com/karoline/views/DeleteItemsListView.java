package com.karoline.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.karoline.utils.SizeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${Karoline} on 2017/4/25.
 */

public class DeleteItemsListView extends ListView {
    private boolean isEditting = false;
    private List<String> datas;
    private Context mContext;
    private MessageAdapter adapter;
    private CheckBox checkBox;
    private TextView textView;
    private List<String> deleteS = new ArrayList<>();
    private DeletesListener mListener;
    private PopupWindow popupWindow;

    public DeleteItemsListView(Context context) {
        super(context);
    }

    public DeleteItemsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        adapter = new MessageAdapter(mContext);
        getFooterView();
    }

    public DeleteItemsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDeletesListener(DeletesListener  listener){
        this.mListener = listener;
    }

    public boolean isEditting(){
        return isEditting;
    }


    public void setDatas(List<String> datas){
        this.datas = datas;
        adapter.notifyDataSetChanged();
        this.setAdapter(adapter);
    }

    public void showEditView(){
        if(datas == null || datas.size()==0){
            Snackbar.make(this,"没有可编辑数据",Snackbar.LENGTH_SHORT).show();
            if(mListener != null) mListener.noDatas();
        }else if(!isEditting){
            isEditting = true;
            if(!popupWindow.isShowing()){
                popupWindow.showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.BOTTOM,0,0);
            }
            adapter.isnothingcheck();
            this.setPadding(0,0,0,SizeUtils.dp2px(mContext,48));
        }
    }

    public void dismissEditView(){
        isEditting = false;
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        adapter.isnothingcheck();
        this.setPadding(0,0,0,0);
    }

    public View getFooterView(){
        LinearLayout footView = new LinearLayout(mContext);
        footView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(mContext,56)));
        footView.setBackgroundColor(Color.LTGRAY);
        footView.setOrientation(LinearLayout.HORIZONTAL);
        footView.setPadding(SizeUtils.dp2px(mContext,16),0,0,0);
        checkBox = new CheckBox(mContext);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT,1));
        checkBox.setChecked(false);
        checkBox.setPadding(SizeUtils.dp2px(mContext,16),0,SizeUtils.dp2px(mContext,16),0);
        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    adapter.isallcheck();
                }else {
                    adapter.isnothingcheck();
                }
                adapter.notifyDataSetChanged();
            }
        });

        textView = new TextView(mContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText("确认删除");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(SizeUtils.dp2px(mContext,16),0,SizeUtils.dp2px(mContext,16),0);
        textView.setBackgroundColor(Color.RED);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteS.clear();
                for(int i=0;i<datas.size();i++){
                    if(adapter.checkStatus.get(i)){
                        deleteS.add(datas.get(i));
                    }
                }
                if(mListener != null){
                    mListener.deleteDatas(deleteS);
                }

                dismissEditView();
            }
        });
        footView.removeAllViews();
        footView.addView(checkBox);
        footView.addView(textView);

        popupWindow = new PopupWindow(footView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(footView);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setFocusable(false);

        return footView;
    }


    public class MessageAdapter extends BaseAdapter {
        private HashMap<Integer, Boolean> checkStatus;

        public MessageAdapter(Context context) {
            checkStatus = new HashMap<Integer, Boolean>();
        }

        public void isallcheck() {
            for (int i = 0; i < datas.size(); i++) {
                checkStatus.put(i, true);
            }
            notifyDataSetChanged();
            textView.setText("确认删除("+getCheckCount()+")");
        }

        public void isnothingcheck() {
            for (int i = 0; i < datas.size(); i++) {
                checkStatus.put(i, false);
            }
            notifyDataSetChanged();
            textView.setText("确认删除");
        }

        public int getCheckCount() {
            int count = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (checkStatus.get(i))
                    count++;
            }
            return count;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MessageCellViewHolder holder;
            if (convertView == null) {
                holder = new MessageCellViewHolder();
                ItemView itemView = new ItemView();
                convertView = itemView.getItemView();
                holder.checkBox = itemView.itemCheck;
                holder.msgContent = itemView.itemText;
                holder.itemview = itemView.getItemView();
                convertView.setTag(holder);
            } else {
                holder = (MessageCellViewHolder) convertView.getTag();
            }

            holder.checkBox.toggle();
            if (isEditting) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }
            if(checkStatus.size()>0){
                holder.checkBox.setChecked(checkStatus.get(position));
            }
            holder.msgContent.setText(datas.get(position));

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkBox.isChecked()) {
                        checkStatus.put(position, true);
                    } else {
                        checkStatus.put(position, false);
                    }
                    if(getCheckCount() == datas.size()){
                        checkBox.setChecked(true);
                    }else {
                        checkBox.setChecked(false);
                    }
                    textView.setText("确认删除("+getCheckCount()+")");
                }
            });

            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            return convertView;
        }

        class MessageCellViewHolder {
           // public AppCompatTextView msgTitle;
            public AppCompatTextView msgContent;
            public AppCompatCheckBox checkBox;
            public View itemview;
        }


    }

    public interface DeletesListener{
        void deleteDatas(List<String> datas);
        void noDatas();
    }

    public class ItemView{
        private AppCompatCheckBox itemCheck;
        private AppCompatTextView itemText;

        private View getItemView(){
            LinearLayout ItemView = new LinearLayout(mContext);
            ItemView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ItemView.setMinimumHeight(SizeUtils.dp2px(mContext,48));
            ItemView.setPadding(SizeUtils.dp2px(mContext,16),0,SizeUtils.dp2px(mContext,16),0);
            ItemView.setOrientation(LinearLayout.HORIZONTAL);
            itemCheck = new AppCompatCheckBox(mContext);
            itemCheck.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT));
            itemCheck.setPadding(0,0,SizeUtils.dp2px(mContext,6),0);

            itemText = new AppCompatTextView(mContext);
            itemText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
            itemText.setBackgroundColor(Color.WHITE);
            itemText.setGravity(Gravity.CENTER);
            ItemView.removeAllViews();
            ItemView.addView(itemCheck);
            ItemView.addView(itemText);

            return ItemView;
        }
    }
}
