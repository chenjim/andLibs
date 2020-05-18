package com.chenjim.andlibs.demo

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.chenjim.andlibs.activity.MvvmActivity
import com.chenjim.andlibs.demo.MainViewModel.IPageView
import com.chenjim.andlibs.demo.databinding.ActivityMainBindingImpl
import com.chenjim.andlibs.demo.fragement.ContainerFragment
import com.chenjim.andlibs.utils.Logger

/**
 * @author jim.chen
 */
class MainActivity : MvvmActivity<ActivityMainBindingImpl?, MainViewModel?>(), IPageView {

    override fun onRetryBtnClick() {}

    override fun createViewModel(): MainViewModel {
        return AndroidViewModelFactory(application).create(MainViewModel::class.java)
    }

    override fun initView() {
        super.initView()
        fragmentReplace(R.id.container, ContainerFragment(), false)
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun doAction() {
        Logger.d()
    }

    override fun requestResponse(data: DataResponse?) {
        Logger.d(data)
    }
}