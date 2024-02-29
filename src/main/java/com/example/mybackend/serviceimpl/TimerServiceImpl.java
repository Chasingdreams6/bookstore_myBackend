package com.example.mybackend.serviceimpl;

import com.example.mybackend.entity.Result;
import com.example.mybackend.service.TimerService;
import com.example.mybackend.utility.Constants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Scope("session")
public class TimerServiceImpl implements TimerService {

    private Date date;

    @Override
    public void initTimer() {
        date = new Date();
    }

    @Override
    public Result<String> getTime() {
        Date nowDate = new Date();
        long dur = nowDate.getTime() - date.getTime();
        long day = dur / (24 * 60 * 60 * 1000);
        long hour = (dur / (60 * 60 * 1000) - day * 24);
        long min = dur / (60 * 1000) - day * 24 * 60 - hour * 60;
        long sec = dur / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        return new Result<>(Constants.SUCCESS, "success to logout",
                "您此次登录" + day + "天" + hour + "小时" + min + "分" + sec + "秒");
    }
}
