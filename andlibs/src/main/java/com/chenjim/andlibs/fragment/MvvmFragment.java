package com.chenjim.andlibs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chenjim.andlibs.R;
import com.chenjim.andlibs.loadsir.EmptyCallback;
import com.chenjim.andlibs.loadsir.ErrorCallback;
import com.chenjim.andlibs.loadsir.LoadingCallback;
import com.chenjim.andlibs.utils.Logger;
import com.chenjim.andlibs.utils.ToastUtil;
import com.chenjim.andlibs.viewmodel.IMvvmBaseViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;


public abstract class MvvmFragment<V extends ViewDataBinding, VM extends IMvvmBaseViewModel>
        extends Fragment implements IBasePagingView {
    protected VM viewModel;
    protected V viewDataBinding;
    protected String mFragmentTag = "";
    private LoadService mLoadService;

    public abstract int getBindingVariable();

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract VM getViewModel();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getViewModel();
        if (viewModel != null) {
            viewModel.attachUI(this);
        }
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }

        initView();

    }


    public void initView() {

    }

    /**
     * @param containerViewId
     * @param fragment
     * @param addToBackStack
     */
    public void fragmentReplace(@IdRes int containerViewId, @NonNull Fragment fragment,
                                boolean addToBackStack) {
        if (getActivity() == null) {
            return;
        }
        FragmentManager manager = getActivity().getSupportFragmentManager();

        FragmentTransaction transaction = manager
                .beginTransaction()
                .replace(containerViewId, fragment, getFragmentTag());
        if (addToBackStack) {
            transaction.addToBackStack(getFragmentTag());
        }
        transaction.commit();
    }


    /***
     *   初始化参数
     */
    protected void initParameters() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            if (!isShowedContent) {
                mLoadService.showCallback(ErrorCallback.class);
            } else {
                ToastUtil.showShort(message);
            }
        }
    }

    @Override
    public void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    private boolean isShowedContent = false;

    @Override
    public void showContent() {
        if (mLoadService != null) {
            isShowedContent = true;
            mLoadService.showSuccess();
        }
    }

    public void onRetryBtnClick() {

    }

    @Override
    public void onLoadMoreFailure(String message) {
        ToastUtil.showShort(getContext(), message);
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtil.showShort(getContext(), getString(R.string.no_more_data));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d(getFragmentTag(), "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        Logger.d(getFragmentTag(), "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (viewModel != null && viewModel.isUIAttached()) {
            viewModel.detachUI();
        }
        Logger.d(getFragmentTag(), "onDetach");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d(getFragmentTag(), "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(getFragmentTag(), "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(getFragmentTag(), "onResume");
    }

    @Override
    public void onDestroy() {
        Logger.d(getFragmentTag(), "onDestroy");
        super.onDestroy();
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

    @Override
    public void onBack() {
        if (getActivity() != null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                manager.popBackStack();
            }
        }
    }

    public abstract String getFragmentTag();
}
