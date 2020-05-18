package com.chenjim.andlibs.demo.fragement

import androidx.lifecycle.ViewModelProvider
import com.chenjim.andlibs.BR
import com.chenjim.andlibs.demo.DataResponse
import com.chenjim.andlibs.demo.R
import com.chenjim.andlibs.demo.databinding.FragmentContainerBindingImpl
import com.chenjim.andlibs.fragment.MvvmFragment
import com.chenjim.andlibs.utils.Logger

/**
 * @description：
 * @fileName: ContainerFragment
 * @author: jim.chen
 * @date: 2020/5/18
 *
 */
class ContainerFragment : MvvmFragment<FragmentContainerBindingImpl?, ContainerViewModel?>()
        , ContainerViewModel.IPageView {


    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_container
    }

    override fun getViewModel(): ContainerViewModel {
        return ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
                .create(ContainerViewModel::class.java)
    }

    override fun getFragmentTag(): String {
        return "ContainerFragment"
    }

    override fun doAction() {
        Logger.d()
    }

    override fun requestResponse(data: DataResponse?) {
        Logger.d(data?.data)
    }

}