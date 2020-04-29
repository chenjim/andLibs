package com.chenjim.andlibs.activity;


public interface IBaseView {
    void showContent();

    void showLoading();

    void onRefreshEmpty();

    void onRefreshFailure(String message);

    void onBack();

}
