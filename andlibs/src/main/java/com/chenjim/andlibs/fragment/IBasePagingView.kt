package com.chenjim.andlibs.fragment

import com.chenjim.andlibs.activity.IBaseView

interface IBasePagingView : IBaseView {
    fun onLoadMoreFailure(message: String?)
    fun onLoadMoreEmpty()
}