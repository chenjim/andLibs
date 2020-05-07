package com.chenjim.andlibs.demo;

import com.chenjim.andlibs.model.BaseModel;
import com.chenjim.andlibs.utils.Logger;

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
        data.data = "123";
        loadSuccess(data);
        loadFail("fail");
        Logger.d("end");
    }
}
