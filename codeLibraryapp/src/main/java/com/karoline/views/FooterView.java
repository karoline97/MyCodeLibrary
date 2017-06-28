package com.karoline.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.china317.syrailway.R;

/**
 * Created by ${Karoline} on 2017/3/21.
 */

public class FooterView extends View{
    private TextView textView;
    private Context mContext;
    public FooterView(Context context) {
        super(context);
        this.mContext = context;
    }

    public View getFooterView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_footer_view,null);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.footerview_progress);
        textView = (TextView) view.findViewById(R.id.footerview_text);

        return view;
    }

    public void setText(String refreshtext){
        textView.setText(refreshtext);
    }
}
