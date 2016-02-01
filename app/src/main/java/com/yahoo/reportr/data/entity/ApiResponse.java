package com.yahoo.reportr.data.entity;

/**
 * Created by bhavanis on 1/4/16.
 */
public class ApiResponse {


    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "data=" + data +
                '}';
    }
}
