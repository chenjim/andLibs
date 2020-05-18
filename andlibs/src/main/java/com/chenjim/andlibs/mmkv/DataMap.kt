package com.chenjim.andlibs.mmkv

import com.tencent.mmkv.MMKV

/**
 * @description：
 * @fileName: MMKV
 * @author: jim.chen
 * @date: 2020/5/20
 *
 */
object DataMap {
    @JvmField
    val base = MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null)

    @JvmField
    val net = MMKV.mmkvWithID("net", MMKV.MULTI_PROCESS_MODE)

    @JvmField
    val ui = MMKV.mmkvWithID("ui", MMKV.MULTI_PROCESS_MODE)

    @JvmField
    val dir = MMKV.getRootDir()

}