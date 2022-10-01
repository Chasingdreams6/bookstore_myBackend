package com.example.mybackend.utility;

import com.alibaba.fastjson.JSON;
import com.example.mybackend.entity.OrderItem;
import com.example.mybackend.entity.Result;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class OrderItemEncoder implements Encoder.Text<Result<OrderItem>>{

    @Override
    public void init(EndpointConfig ec) {}

    @Override
    public void destroy() {}

    @Override
    public String encode(Result<OrderItem> res) {
        return JSON.toJSONString(res);
    }
}
