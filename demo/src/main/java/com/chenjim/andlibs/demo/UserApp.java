package com.chenjim.andlibs.demo;

import com.chenjim.andlibs.BaseApplication;
import com.chenjim.andlibs.loadsir.CustomCallback;
import com.chenjim.andlibs.loadsir.EmptyCallback;
import com.chenjim.andlibs.loadsir.ErrorCallback;
import com.chenjim.andlibs.loadsir.LoadingCallback;
import com.chenjim.andlibs.loadsir.TimeoutCallback;
import com.chenjim.andlibs.preference.SPUtil;
import com.chenjim.andlibs.utils.Logger;
import com.kingja.loadsir.core.LoadSir;

/**
 * @description：
 * @fileName: UserApp
 * @author: jim.chen
 * @date: 2020/4/16
 */
public class UserApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SPUtil.init(this);
        //ApiBase.setNetworkRequestInfo(new NetworkRequestInfo());
        setIsDebug(BuildConfig.DEBUG);
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();

        Logger.init(this, true, true);

//        CC.enableDebug(BuildConfig.DEBUG);
//        CC.enableVerboseLog(BuildConfig.DEBUG);
//        CC.enableRemoteCC(BuildConfig.DEBUG);
    }
}
