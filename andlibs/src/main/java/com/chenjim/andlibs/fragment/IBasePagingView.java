package com.chenjim.andlibs.fragment;


import com.chenjim.andlibs.activity.IBaseView;

public interface IBasePagingView extends IBaseView {

    void onLoadMoreFailure(String message);

    void onLoadMoreEmpty();
}
