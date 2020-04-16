package com.chenjim.andlibs.model;

import java.io.Serializable;

public class BaseCachedData<T> implements Serializable {
    public long updateTimeInMills;
    public T data;
}
