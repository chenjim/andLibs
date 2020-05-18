package com.chenjim.andlibs.demo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.chenjim.andlibs.customview.BaseCustomView
import com.chenjim.andlibs.demo.databinding.ViewTitleBinding
import com.chenjim.andlibs.utils.Logger
import com.chenjim.andlibs.utils.TimeUtils

/**
 * @description：
 * @fileName: TitleView
 * @author: jim.chen
 * @date: 2020/5/16
 *
 */
class TitleView : BaseCustomView<ViewTitleBinding?, TitleViewModel?> {

    constructor(context: Context?) : super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    init {
        Logger.d(TimeUtils.getCurDate2())
    }

    override val viewLayoutId: Int
        get() = R.layout.view_title

    override fun bindingViewModel(data: TitleViewModel?) {
        dataBinding?.viewModel = data;
    }

    override fun onRootClick(view: View?) {

    }


}
