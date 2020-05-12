package com.chenjim.andlibs.demo;

import com.chenjim.andlibs.bean.BaseItem;

/**
 * @description：
 * @fileName: ResponseData
 * @author: jim.chen
 * @date: 2020/5/7
 */
public class DataResponse extends BaseItem {
    public String data;

    @Override
    public String toString() {
        return "ResponseData{" +
                "data='" + data + '\'' +
                '}';
    }
}
