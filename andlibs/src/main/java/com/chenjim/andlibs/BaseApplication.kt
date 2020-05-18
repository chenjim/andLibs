package com.chenjim.andlibs

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.tencent.mmkv.MMKV

open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        sApp = this
        MMKV.initialize(this)
    }

    companion object {
        @JvmField
        var sApp: BaseApplication? = null

        @JvmField
        var sDebug = false

        /**
         * 获取进程名
         *
         * @param context
         * @return
         */
        fun getCurrentProcessName(context: Context): String? {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses ?: return null
            for (proInfo in runningApps) {
                if (proInfo.pid == Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName
                    }
                }
            }
            return null
        }
    }
}