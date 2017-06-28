package com.karoline.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.china317.developlibrary.utils.DisplayUtil;
import com.china317.syrailway.ui.contract.response.AttachmentRes;

/**
 * Created by ${Karoline} on 2017/5/19.
 */

public class MytextView extends TextView implements View.OnClickListener{
    private AttachmentRes.AttachFile cFile;

    public MytextView(Context context) {
        super(context);
        init(context);
    }

    public MytextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MytextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        setTextSize(13);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        setMinHeight((int) DisplayUtil.DipToPx(context,26));
        setTextColor(Color.DKGRAY);
        setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        setOnClickListener(this);
    }

    public void setValue(AttachmentRes.AttachFile file){
        cFile = file;
    }

    @Override
    public void onClick(View view) {
        if(cFile != null){
            onValueChangedListner.OnValueChanged(cFile);
        }
    }

    public interface OnTextChickListner{
        void OnValueChanged(AttachmentRes.AttachFile file);
    }

    //实现接口，方便将当前按钮的值回调
    OnTextChickListner onValueChangedListner;

    public void setOnTextChickListner(OnTextChickListner onValueChangedListner){
        this.onValueChangedListner = onValueChangedListner;
    }
}
