package com.example.mybackend.service;

import com.example.mybackend.entity.Result;

public interface TimerService {
    public void initTimer();
    public Result<String> getTime();
}
