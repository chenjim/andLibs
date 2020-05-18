package com.chenjim.andlibs.viewmodel

interface IMvvmBaseViewModel<V> {
    fun attachUI(view: V)
    val pageView: V
    val isUIAttached: Boolean
    fun detachUI()
    fun onBack()
}