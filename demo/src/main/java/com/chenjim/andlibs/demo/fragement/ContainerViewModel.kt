package com.chenjim.andlibs.demo.fragement

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.chenjim.andlibs.activity.IBaseView
import com.chenjim.andlibs.demo.DataResponse
import com.chenjim.andlibs.demo.MainModel
import com.chenjim.andlibs.model.BaseModel
import com.chenjim.andlibs.utils.Logger
import com.chenjim.andlibs.utils.TimeUtils
import com.chenjim.andlibs.utils.ToastUtil
import com.chenjim.andlibs.viewmodel.MvvmBaseViewModel

/**
 * @description：
 * @fileName: ContainerViewModel
 * @author: jim.chen
 * @date: 2020/5/18
 *
 */

class ContainerViewModel(application: Application)
    : MvvmBaseViewModel<ContainerViewModel.IPageView?, MainModel?>(application)
        , BaseModel.IModelListener<DataResponse?> {

    init {
        model = MainModel()
        model!!.register(this)
        Logger.d(this)
    }

    override fun loadSuccess(model: BaseModel<*>?, data: DataResponse?) {
        Logger.d(data)
        time.value = data?.data
        ToastUtil.showShort(data?.data)
        pageView!!.requestResponse(data)
    }

    override fun loadFail(model: BaseModel<*>?, prompt: String?) {
        Logger.d(prompt)
    }

    @JvmField
    var time = MutableLiveData(TimeUtils.getCurTime())


    fun onClick() {
        Logger.d(this)
        pageView?.doAction()
        model?.load()
    }


    interface IPageView : IBaseView {
        fun doAction()
        fun requestResponse(data: DataResponse?)
    }

}