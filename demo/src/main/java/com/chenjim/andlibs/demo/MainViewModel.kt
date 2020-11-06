package com.chenjim.andlibs.demo

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.chenjim.andlibs.activity.IBaseView
import com.chenjim.andlibs.demo.MainViewModel.IPageView
import com.chenjim.andlibs.model.BaseModel
import com.chenjim.andlibs.utils.Logger
import com.chenjim.andlibs.utils.TimeUtils
import com.chenjim.andlibs.utils.ToastUtil
import com.chenjim.andlibs.viewmodel.MvvmBaseViewModel

/**
 * @description：
 * @fileName: MainViewModel
 * @author: jim.chen
 * @date: 2020/5/7
 */
class MainViewModel(application: Application) : MvvmBaseViewModel<IPageView?, MainModel?>(application), BaseModel.IModelListener<DataResponse?> {
    @JvmField
    var title = TitleViewModel("title default")

    @JvmField
    var time = MutableLiveData(TimeUtils.getCurTime())
    fun onClick() {
        Logger.d(this)
        pageView?.doAction()
        model?.load()
    }

    fun clickLeft(view: View) {
        Logger.d()
    }

    override fun loadSuccess(model: BaseModel<*>?, data: DataResponse?) {
        Logger.d(data)
        time.value = data?.data
        title.title = "title:" + data?.data
        ToastUtil.showShort(data?.data)
        pageView!!.requestResponse(data)
    }

    override fun loadFail(model: BaseModel<*>?, prompt: String) {
        Logger.d(prompt)
    }

    interface IPageView : IBaseView {
        fun doAction()
        fun requestResponse(data: DataResponse?)
    }

    init {
        model = MainModel()
        model!!.register(this)
        Logger.d()
    }

}