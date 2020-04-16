package com.chenjim.andlibs.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;


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

    @Nullable
    public V getPageView() {
        if (mUIRef == null) {
            return null;
        }
        return mUIRef.get();
    }

    public boolean isUIAttached() {
        return mUIRef != null && mUIRef.get() != null;
    }

    public void detachUI() {
        if (mUIRef != null) {
            mUIRef.clear();
            mUIRef = null;

        }
        if (model != null) {
            model.cancel();
        }
    }
}
