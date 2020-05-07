package com.chenjim.andlibs.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.chenjim.andlibs.R;


public class LoadingDialog extends ProgressDialog {
    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialogStyle);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.LoadingDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.layout_loading_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }


}
