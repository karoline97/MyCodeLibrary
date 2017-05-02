package com.karoline.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

/**
 * Created by ${Karoline} on 2017/4/21.
 */

public class AutoComplateEditTextView<T> extends EditText {
    private Context mContext;
    private PopupWindow popupWindow;
    private List<T> datas;
    private List<String> stringList;
    private ArrayAdapter<String> adapter;
    private Handler mHandler;
    private Runnable requestRunable;
    private String beforeText;
    private boolean isItemClick;

    private static ClickItemAndTextChangedLisener mListener;

    public AutoComplateEditTextView(Context context) {
        super(context);
    }

    public AutoComplateEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPopupView();
        this.addTextChangedListener(new EditTextChange());
    }

    public AutoComplateEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setClickItemAndTextChangedLisener(ClickItemAndTextChangedLisener lisener){
        mListener = lisener;
    }

    public void setDatas(List<T> datas,List<String> stringList) {
        this.datas = datas;
        this.stringList = stringList;
        showDrop(AutoComplateEditTextView.this);
    }

    public void setHandlerandRequest(Handler handler, Runnable request) {
        this.mHandler = handler;
        this.requestRunable = request;
    }

    private void initPopupView() {
        LinearLayout popupContainer = new LinearLayout(mContext);
        popupContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        popupContainer.setPadding(dp2px(mContext, 16), 0, dp2px(mContext, 16), 0);
        popupContainer.setBackgroundColor(Color.LTGRAY);
        popupContainer.setOrientation(LinearLayout.VERTICAL);
        ListView mListView = new ListView(mContext);
        mListView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));
        mListView.setVerticalScrollBarEnabled(false);
        popupContainer.removeAllViews();
        popupContainer.addView(mListView);

        popupWindow = new PopupWindow(popupContainer, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(popupContainer);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setFocusable(true);

        adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas != null && datas.size() > 0) {
                    dissDrop();
                    isItemClick = true;
                    if (mListener != null) {
                        mListener.onClickItem(datas.get(position));
                    }
                }
            }
        });
    }

    public void showDrop(AutoComplateEditTextView view) {
        if(adapter == null){
            new HandlerNullException("请设置AutoAdapter");
        }else if (!popupWindow.isShowing() && adapter != null && datas != null && datas.size()>0) {
            adapter.clear();
            adapter.addAll(stringList);
            popupWindow.showAsDropDown(view,0,0);
            hideKeyboard(view.getWindowToken());
        }
    }

    public void dissDrop() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }


    public interface ClickItemAndTextChangedLisener<T> {
        void onClickItem(T data);
        void textChanged(String text);
    }

    private static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public class HandlerNullException {
        HandlerNullException(String msg) {
            Snackbar.make(AutoComplateEditTextView.this, msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    class EditTextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("beforeTextChanged:",s.toString() +"start:"+start+"count"+count+"after:"+after);
            beforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("onTextChanged:",s.toString() +"start:"+start+"before"+before+"count:"+count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d("afterTextChanged:",s.toString());
            String  newText = s.toString();
            Log.d("newText:",newText);
            if (mHandler == null) {
                new HandlerNullException("请输入Handler对象");
            }

            if(TextUtils.isEmpty(newText) || newText.equals(beforeText) || isItemClick){
                if (requestRunable != null) {
                    mHandler.removeCallbacks(requestRunable);
                }
                isItemClick = false;
            }else {
                if (requestRunable != null) {
                    mHandler.removeCallbacks(requestRunable);
                }
                if (mListener != null) {
                    mListener.textChanged(newText);
                }
                mHandler.postDelayed(requestRunable, 800);
            }
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dissDrop();
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
