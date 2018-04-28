package com.grad.model;

import com.grad.common.IRequest;


public class ParamRequest implements IRequest{
    public String data;
    public long time;

    public long getTime() {
        return time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
