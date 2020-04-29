package com.chenjim.andlibs.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.chenjim.andlibs.activity.IBaseView;
import com.chenjim.andlibs.model.SuperBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public class MvvmBaseViewModel<V, M extends SuperBaseModel> extends ViewModel implements IMvvmBaseViewModel<V> {
    private Reference<V> mUIRef;
    protected M model;

    @Override
    public void attachUI(V ui) {
        mUIRef = new WeakReference<>(ui);
    }

    @Override
    @Nullable
    public V getPageView() {
        if (mUIRef == null) {
            return null;
        }
        return mUIRef.get();
    }

    @Override
    public boolean isUIAttached() {
        return mUIRef != null && mUIRef.get() != null;
    }

    @Override
    public void detachUI() {
        if (mUIRef != null) {
            mUIRef.clear();
            mUIRef = null;

        }
        if (model != null) {
            model.cancel();
        }
    }

    @Override
    public void onBack() {
        if (mUIRef != null) {
            if (mUIRef.get() instanceof IBaseView) {
                ((IBaseView) mUIRef.get()).onBack();
            }
        }
    }
}
