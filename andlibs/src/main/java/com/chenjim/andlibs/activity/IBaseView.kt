package com.chenjim.andlibs.activity

interface IBaseView {
    fun showContent()
    fun showLoading()
    fun onRefreshEmpty()
    fun onRefreshFailure(message: String?)
    fun onBack()
}