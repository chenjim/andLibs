package com.chenjim.andlibs.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chenjim.andlibs.activity.MvvmActivity;
import com.chenjim.andlibs.demo.databinding.ActivityMainBindingImpl;
import com.chenjim.andlibs.utils.Logger;


/**
 * @author jim.chen
 */
public class MainActivity extends MvvmActivity<ActivityMainBindingImpl, MainViewModel>
        implements MainViewModel.IPageView {

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
    protected MainViewModel getViewModel() {
        return new MainViewModel();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void doAction() {
        Logger.d();
    }

    @Override
    public void requestResponse(DataResponse data) {
        Logger.d(data);
    }

}