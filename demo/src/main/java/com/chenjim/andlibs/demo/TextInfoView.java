package com.chenjim.andlibs.demo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @description：
 * @fileName: TextInfoView
 * @author: jim.chen
 * @date: 2020/1/20
 */
public class TextInfoView extends AppCompatTextView {


    public TextInfoView(Context context) {
        super(context);
        init(context);
    }

    public TextInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setTextSize(60);
        setTextColor(context.getResources().getColor(android.R.color.black));

    }
}
