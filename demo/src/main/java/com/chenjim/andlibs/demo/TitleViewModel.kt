package com.chenjim.andlibs.demo

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.chenjim.andlibs.customview.BaseCustomViewModel
import com.chenjim.andlibs.utils.Logger

/**
 * @description：
 * @fileName: TitleViewModel
 * @author: jim.chen
 * @date: 2020/5/16
 *
 */
class TitleViewModel : BaseCustomViewModel {

    @Bindable
    var title: String = "title name"
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }


    constructor(title: String) : super() {
        this.title = title
    }

    fun onBackClick() {
        Logger.d()
    }

}

@BindingAdapter("bindingTitle")
fun bindTitle(view: TitleView, data: TitleViewModel) {
    view.setData(data)
}

