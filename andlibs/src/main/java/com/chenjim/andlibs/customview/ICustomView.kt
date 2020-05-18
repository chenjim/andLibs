package com.chenjim.andlibs.customview

interface ICustomView<S : BaseCustomViewModel?> {
    fun setData(data: S)
    fun setStyle(resId: Int)
    fun setActionListener(listener: ICustomViewActionListener?)
}