package com.chenjim.andlibs.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chenjim.andlibs.activity.MvvmActivity;
import com.chenjim.andlibs.viewmodel.IMvvmBaseViewModel;


/**
 * @author jim.chen
 */
public class MainActivity extends MvvmActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected IMvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

}