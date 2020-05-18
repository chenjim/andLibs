package com.chenjim.andlibs.customview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open abstract class BaseCustomView<T : ViewDataBinding?, S : BaseCustomViewModel?> : LinearLayout, ICustomView<S>, View.OnClickListener {
    protected var dataBinding: T? = null
        private set
    protected var viewModel: S? = null
        private set
    private var mListener: ICustomViewActionListener? = null

    public constructor(context: Context?) : super(context) {
        init()
    }

    public constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    public constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    override fun getRootView(): View {
        return dataBinding!!.root
    }

    fun init() {
        val inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (viewLayoutId != 0) {
            dataBinding = DataBindingUtil.inflate<T>(inflater, viewLayoutId, this, false)
            dataBinding?.getRoot()?.setOnClickListener { view ->
                if (mListener != null) {
                    mListener!!.onAction(ICustomViewActionListener.ACTION_ROOT_VIEW_CLICKED, view, viewModel)
                }
                onRootClick(view)
            }
            this.addView(dataBinding?.getRoot())
        }
    }

    override fun setData(data: S) {
        viewModel = data
        bindingViewModel(viewModel)
        dataBinding?.executePendingBindings()
        onDataUpdated()
    }

    protected fun onDataUpdated() {}
    override fun setStyle(resId: Int) {}
    override fun setActionListener(listener: ICustomViewActionListener?) {
        mListener = listener
    }

    override fun onClick(v: View) {}

    protected abstract val viewLayoutId: Int
    protected abstract fun bindingViewModel(data: S?)
    protected abstract fun onRootClick(view: View?)
}