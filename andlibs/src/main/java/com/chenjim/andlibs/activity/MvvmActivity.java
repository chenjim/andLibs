package com.chenjim.andlibs.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chenjim.andlibs.loadsir.EmptyCallback;
import com.chenjim.andlibs.loadsir.ErrorCallback;
import com.chenjim.andlibs.loadsir.LoadingCallback;
import com.chenjim.andlibs.viewmodel.IMvvmBaseViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;


public abstract class MvvmActivity<V extends ViewDataBinding, VM extends IMvvmBaseViewModel> extends AppCompatActivity implements IBaseView {
    protected VM viewModel;
    private LoadService mLoadService;
    protected V viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
    }

    private void initViewModel() {
        viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.attachUI(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }
    }

    @Override
    public void onRefreshEmpty() {
        if (mLoadService != null) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }

    @Override
    public void onRefreshFailure(String message) {
        if (mLoadService != null) {
            mLoadService.showCallback(ErrorCallback.class);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void showContent() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    public void setLoadSir(View view) {
        // You can change the callback on sub thread directly.
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
    }

    protected abstract void onRetryBtnClick();

    protected abstract VM getViewModel();

    public abstract int getBindingVariable();

    public abstract
    @LayoutRes
    int getLayoutId();

    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
    }
}
