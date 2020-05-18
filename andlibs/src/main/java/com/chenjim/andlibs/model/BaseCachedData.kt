package com.chenjim.andlibs.model

import java.io.Serializable

class BaseCachedData<T> : Serializable {
    @JvmField
    var updateTimeInMills: Long = 0
    @JvmField
    var data: T? = null
}