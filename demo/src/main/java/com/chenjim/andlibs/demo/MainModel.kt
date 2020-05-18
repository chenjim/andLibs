package com.chenjim.andlibs.demo

import com.chenjim.andlibs.model.BaseModel
import com.chenjim.andlibs.utils.Logger
import com.chenjim.andlibs.utils.TimeUtils

/**
 * @description：
 * @fileName: MainModel
 * @author: jim.chen
 * @date: 2020/5/7
 */
class MainModel : BaseModel<DataResponse?>() {
    override fun refresh() {}
    public override fun load() {
        Logger.d("start")
        val data = DataResponse()
        data.data = TimeUtils.getCurTime()
        loadSuccess(data)
        loadFail("fail")
        Logger.d("end")
    }
}