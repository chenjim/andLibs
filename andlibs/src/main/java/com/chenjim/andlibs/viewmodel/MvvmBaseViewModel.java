package com.chenjim.andlibs.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.chenjim.andlibs.activity.IBaseView;
import com.chenjim.andlibs.model.SuperBaseModel;
import com.chenjim.andlibs.utils.Logger;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public class MvvmBaseViewModel<V, M extends SuperBaseModel> extends AndroidViewModel implements IMvvmBaseViewModel<V> {
    private Reference<V> mUIRef;
    protected M model;

    public MvvmBaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void attachUI(V ui) {
        mUIRef = new WeakReference<>(ui);
        Logger.d(ui);
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
            Logger.d(mUIRef.get());
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
