package com.chenjim.andlibs.demo;

import com.chenjim.andlibs.activity.IBaseView;
import com.chenjim.andlibs.model.BaseModel;
import com.chenjim.andlibs.utils.Logger;
import com.chenjim.andlibs.viewmodel.MvvmBaseViewModel;

/**
 * @description：
 * @fileName: MainViewModel
 * @author: jim.chen
 * @date: 2020/5/7
 */
public class MainViewModel extends MvvmBaseViewModel<MainViewModel.IPageView, MainModel>
        implements BaseModel.IModelListener<DataResponse> {

    public MainViewModel() {
        model = new MainModel();
        model.register(this);
    }

    public void onClick() {
        Logger.d();
        getPageView().doAction();
        model.load();
    }

    @Override
    public void responseSuccess(BaseModel model, DataResponse data) {
        Logger.d(data);
        getPageView().requestResponse(data);
    }

    @Override
    public void responseFail(BaseModel model, String prompt) {
        Logger.d(prompt);
    }


    public interface IPageView extends IBaseView {
        void doAction();

        void requestResponse(DataResponse data);
    }
}
