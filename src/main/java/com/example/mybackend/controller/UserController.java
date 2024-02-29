package com.example.mybackend.controller;

import com.example.mybackend.service.TimerService;
import com.example.mybackend.serviceimpl.TimerServiceImpl;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.entity.MostCostUser;
import com.example.mybackend.entity.Result;
import com.example.mybackend.entity.User;
import com.example.mybackend.service.UserService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

//    @Autowired
//    private  TimerServiceImpl timerService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @RequestMapping(value = "/getUsers", produces="application/json;charset=UTF-8")
    public Result<List<User>> getUsers() {return userService.getUsers();}
    /*
    * register
    * */
    @PostMapping(value = "/register")
    public Result<User> register(@RequestBody Map<String, String> params)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String id = params.get(Constants.USERID);
        User user = new User(
                    id == null ? null :Integer.parseInt(id),
                    params.get(Constants.USERNAME),
                    params.get(Constants.PASSWORD),
                    params.get(Constants.GENDER),
                    params.get(Constants.MAIL),
                    params.get(Constants.ADDRESS),
                    params.get(Constants.PHONE),
                1
                );
        logger.info("registering... user:" + user.toString());
        return userService.register(user);
    }

    /*
    * login
    * */
   // @CrossOrigin(maxAge = 3600,origins = "*")
    @PostMapping(value = "/login")
    public Result<User> login(@RequestBody Map<String, String> params)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String id = params.get(Constants.USERID);
        User user = new User(
                id == null ? null :Integer.parseInt(id),
                params.get(Constants.USERNAME),
                params.get(Constants.PASSWORD),
                params.get(Constants.GENDER),
                params.get(Constants.MAIL),
                params.get(Constants.ADDRESS),
                params.get(Constants.PHONE),
                0
        );
        //logger.info("login.. user:" + user.toString());
        Result<User> result = userService.login(user);
        if (result.getCode() == Constants.SUCCESS) {
            TimerService timerService = webApplicationContext.getBean(TimerService.class);
            timerService.initTimer();
            //System.out.println("init timer" + timerService);
            //System.out.println(this);
        }
        return result;
    }

    @PostMapping(value = "/logout")
    public Result<String> logout(@RequestBody Map<String, String> params)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String id = params.get(Constants.USERID);
        User user = new User(
                id == null ? null :Integer.parseInt(id),
                params.get(Constants.USERNAME),
                params.get(Constants.PASSWORD),
                params.get(Constants.GENDER),
                params.get(Constants.MAIL),
                params.get(Constants.ADDRESS),
                params.get(Constants.PHONE),
                0
        );
        //logger.info("login.. user:" + user.toString());
        TimerService timerService = webApplicationContext.getBean(TimerService.class);
        //System.out.println(timerService);
        //System.out.println(this);
        return timerService.getTime();
    }

   // @CrossOrigin
    @RequestMapping("/updateUser")
    public Result<User> updateUser(@RequestBody Map<String, String> params)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String id = params.get(Constants.USERID);
        User user = new User(
                id == null ? null :Integer.parseInt(id),
                params.get(Constants.USERNAME),
                params.get(Constants.PASSWORD),
                params.get(Constants.GENDER),
                params.get(Constants.MAIL),
                params.get(Constants.ADDRESS),
                params.get(Constants.PHONE),
                Integer.parseInt(params.get(Constants.VALIDUSER))
                );
        return userService.update(user);
    }

    @RequestMapping("/mostCostUser")
    public Result<User> mostCostUser() {
        return userService.mostCostUser();
    }

    @RequestMapping("/usersSortedByCost")
    public Result<List<MostCostUser>> usersSortedByCost(
            @RequestParam(Constants.STARTUSERTIME) String start,
            @RequestParam(Constants.ENDUSERTIME) String end){
        return userService.usersSortedByCost(start, end);
    }

    @RequestMapping("/banUser")
    public Result<User> banUser(@RequestParam(Constants.USERID) Integer userid) {
        return userService.banUser(userid);
    }

    @RequestMapping("/unBanUser")
    public Result<User> unBanUser(@RequestParam(Constants.USERID) Integer userid) {
        return userService.unBanUser(userid);
    }
}
