package com.chenjim.andlibs.demo;

import com.blankj.utilcode.util.TimeUtils;
import com.chenjim.andlibs.model.BaseModel;
import com.chenjim.andlibs.utils.Logger;

import java.util.Date;

/**
 * @description：
 * @fileName: MainModel
 * @author: jim.chen
 * @date: 2020/5/7
 */
public class MainModel extends BaseModel<DataResponse> {
    @Override
    public void refresh() {

    }

    @Override
    protected void load() {
        Logger.d("start");
        DataResponse data = new DataResponse();
        data.data = TimeUtils.date2String(new Date());
        loadSuccess(data);
        loadFail("fail");
        Logger.d("end");
    }
}
