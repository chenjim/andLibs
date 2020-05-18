package com.chenjim.andlibs.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BusUtils
import com.chenjim.andlibs.loadsir.EmptyCallback
import com.chenjim.andlibs.loadsir.ErrorCallback
import com.chenjim.andlibs.loadsir.LoadingCallback
import com.chenjim.andlibs.viewmodel.IMvvmBaseViewModel
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

abstract class MvvmActivityBak<V : ViewDataBinding?, VM : IMvvmBaseViewModel<Any>?> : AppCompatActivity(), IBaseView {
    protected var viewModel: VM? = null
    private var mLoadService: LoadService<*>? = null
    var viewDataBinding: V? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        performDataBinding()
        BusUtils.register(this)
        initView()
    }

    fun initView() {}

    private fun initViewModel() {
        viewModel = createViewModel()
        viewModel?.attachUI(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel?.isUIAttached == true) {
            viewModel?.detachUI()
        }
        BusUtils.unregister(this)
    }

    override fun onRefreshEmpty() {
        mLoadService?.showCallback(EmptyCallback::class.java)
    }

    override fun onRefreshFailure(message: String?) {
        mLoadService?.showCallback(ErrorCallback::class.java)
    }

    override fun showLoading() {
        mLoadService?.showCallback(LoadingCallback::class.java)
    }

    override fun showContent() {
        mLoadService?.showSuccess()
    }

    /**
     * @param containerViewId
     * @param fragment
     * @param addToBackStack
     */
    fun fragmentReplace(@IdRes containerViewId: Int, fragment: Fragment,
                        addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun setLoadSir(view: View?) {
        // You can change the callback on sub thread directly.
        mLoadService = LoadSir.getDefault().register(view) { onRetryBtnClick() }
    }

    protected abstract fun onRetryBtnClick()
    protected abstract fun createViewModel(): VM
    protected abstract fun getBindingVariable(): Int


    protected abstract fun getLayoutId(): Int

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView<V>(this, getLayoutId())
        if (getBindingVariable() > 0) {
            viewDataBinding?.setVariable(getBindingVariable(), viewModel)
        }
        viewDataBinding?.lifecycleOwner = this
        viewDataBinding?.executePendingBindings()
    }

    override fun onBack() {
        onBackPressed()
    }
}