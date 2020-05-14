package com.chenjim.andlibs.demo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.TimeUtils;
import com.chenjim.andlibs.activity.IBaseView;
import com.chenjim.andlibs.model.BaseModel;
import com.chenjim.andlibs.utils.Logger;
import com.chenjim.andlibs.utils.ToastUtil;
import com.chenjim.andlibs.viewmodel.MvvmBaseViewModel;

import java.util.Date;

/**
 * @description：
 * @fileName: MainViewModel
 * @author: jim.chen
 * @date: 2020/5/7
 */
public class MainViewModel extends MvvmBaseViewModel<MainViewModel.IPageView, MainModel>
        implements BaseModel.IModelListener<DataResponse> {

    public MutableLiveData<String> time = new MutableLiveData<>(TimeUtils.date2String(new Date()));

    public MainViewModel(@NonNull Application application) {
        super(application);
        model = new MainModel();
        model.register(this);
        Logger.d(this);
    }

    public void onClick() {
        Logger.d(this);
        getPageView().doAction();
        model.load();
    }

    @Override
    public void loadSuccess(BaseModel model, DataResponse data) {
        Logger.d(data);
        this.time.setValue(data.data);
        ToastUtil.showShort(data.data);
        getPageView().requestResponse(data);
    }

    @Override
    public void loadFail(BaseModel model, String prompt) {
        Logger.d(prompt);
    }

    public interface IPageView extends IBaseView {
        void doAction();

        void requestResponse(DataResponse data);
    }
}
