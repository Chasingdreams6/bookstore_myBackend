package com.example.mybackend.serviceimpl;


import com.example.mybackend.dao.UserDao;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.entity.MostCostUser;
import com.example.mybackend.entity.Result;
import com.example.mybackend.entity.User;
import com.example.mybackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class) // 事务回滚
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao; //

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private Result<User> mResult = new Result<>();
    private Result<List<User>> result = new Result<>();
    private Result<List<MostCostUser>> result2 = new Result<>();

    private void restart() {
        mResult.setCode(-1);
        mResult.setMsg("");
        mResult.setDetail(null);
    }
    /*
     * register
     * */
    public Result<User> register(User user) {
        restart();
        User selectUser = userDao.SearchByName(user.getUsername());
//        User test = userDao.SearchByName("JinBo");
//        if (test == null) System.out.println("find method error");
        if (selectUser == null) { // to register
            int insertFlag = userDao.insert(user);
            if (insertFlag >= 1) { // success
                mResult.setCode(Constants.SUCCESS);
                mResult.setMsg("success to register");
                //User finalUser = userDao.SearchByName(user.getUsername());
                user.setId(insertFlag);
                //if (finalUser == null) System.out.println("save/use error");
                mResult.setDetail(user);
            }
            else {
                mResult.setCode(Constants.FAIL);
                mResult.setMsg("the username has been used!");
            }
        }
        return mResult;
    }


    /*
     * Login
     * @para  user
     * @return Result
     * */
    public Result<User> login(User user) {
        restart();
        User selectUser = userDao.SearchByName(user.getUsername());
        if (selectUser.getValid() == 0) { // 0 没有权限
            mResult.setCode(Constants.USER_BANNED);
            mResult.setMsg("user banned!");
            mResult.setDetail(selectUser);
        }
        else if (selectUser != null &&
                user.getUserpassword().equals(selectUser.getUserpassword())) {
            if (selectUser.getValid() == 1) { // normal user
                mResult.setCode(Constants.SUCCESS);
                mResult.setMsg("success to login");
                mResult.setDetail(selectUser);
            }
            if (selectUser.getValid() == 2) { // admin
                mResult.setCode(Constants.ADMIN);
                mResult.setMsg("Welcome Admin!");
                mResult.setDetail(selectUser);
            }
        }
        else {
            mResult.setCode(Constants.FAIL);
            mResult.setMsg("fail to login");
        }
        if (selectUser == null) logger.info("search result is empty!");
        else logger.info("search result: " + selectUser.toString());
        return mResult;
    }

    public Result<String> logout(User user) {
        User selectUser = userDao.SearchByName(user.getUsername());
        if (selectUser != null) {
            TimerServiceImpl timerService = webApplicationContext.getBean(TimerServiceImpl.class);
            return timerService.getTime();
        }
        return new Result<>(Constants.FAIL, "username not found", "");
    }

    public Result<User> update(User user) {
        restart();
        int res = userDao.update(user);
        if (res < 1) { // fail
            mResult.setCode(Constants.FAIL);
            mResult.setMsg("Fail to update user");
        }
        else {
            mResult.setCode(Constants.SUCCESS);
            mResult.setMsg("Success to update user");
            mResult.setDetail(user);
        }
        return mResult;
    }

    public Result<List<User>> getUsers() {
        List<User> users = userDao.getUsers();
        if (users == null) {
            result.setCode(Constants.FAIL);
            result.setMsg("No users");
            result.setDetail(null);
        }
        else {
            result.setDetail(users);
            result.setCode(Constants.SUCCESS);
            result.setMsg("Success to get all users");
        }
        return result;
    }

    public Result<User> mostCostUser() {
        User user = userDao.mostCostUser();
        if (user != null) {
            mResult.setDetail(user);
            mResult.setCode(Constants.SUCCESS);
            mResult.setMsg("This is the most cost user");
        }
        else {
            mResult.setCode(Constants.FAIL);
            mResult.setMsg("No users");
            mResult.setDetail(null);
        }
        return mResult;
    }

    public Result<User> banUser(Integer userid) {
        User user = userDao.SearchByID(userid);
        user.setValid(0);
        return update(user);
    }

    public Result<User> unBanUser(Integer userid) {
        User user = userDao.SearchByID(userid);
        user.setValid(1);
        return update(user);
    }

    @Override
    public Result<List<MostCostUser>> usersSortedByCost(String start, String end) {
        List<MostCostUser> users = userDao.usersSortedByCost(start, end);
        if (users != null) {
            result2.setDetail(users);
            result2.setCode(Constants.SUCCESS);
            result2.setMsg("users sorted by cost");
        }
        else {
            result2.setCode(Constants.FAIL);
            result2.setMsg("No users");
            result2.setDetail(null);
        }
        return result2;
    }
}
