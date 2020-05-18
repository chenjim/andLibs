package com.chenjim.andlibs.customview

import android.view.View

interface ICustomViewActionListener {
    fun onAction(action: String?, view: View?, viewModel: BaseCustomViewModel?)

    companion object {
        const val ACTION_ROOT_VIEW_CLICKED = "action_root_view_clicked"
    }
}