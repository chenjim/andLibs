package com.chenjim.andlibs.demo

import com.chenjim.andlibs.BaseApplication
import com.chenjim.andlibs.loadsir.*
import com.chenjim.andlibs.preference.SPUtil
import com.chenjim.andlibs.utils.Logger
import com.kingja.loadsir.core.LoadSir

/**
 * @description：
 * @fileName: UserApp
 * @author: jim.chen
 * @date: 2020/4/16
 */
class UserApp : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        SPUtil.init(this)
        //ApiBase.setNetworkRequestInfo(new NetworkRequestInfo());
        sDebug = BuildConfig.DEBUG
        LoadSir.beginBuilder()
                .addCallback(ErrorCallback()) //添加各种状态页
                .addCallback(EmptyCallback())
                .addCallback(LoadingCallback())
                .addCallback(TimeoutCallback())
                .addCallback(CustomCallback())
                .setDefaultCallback(LoadingCallback::class.java) //设置默认状态页
                .commit()
        Logger.init(this, true, true)

//        CC.enableDebug(BuildConfig.DEBUG);
//        CC.enableVerboseLog(BuildConfig.DEBUG);
//        CC.enableRemoteCC(BuildConfig.DEBUG);
    }
}