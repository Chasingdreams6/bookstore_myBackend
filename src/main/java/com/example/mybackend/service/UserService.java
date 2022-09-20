package com.example.mybackend.service;

import com.example.mybackend.entity.MostCostUser;
import com.example.mybackend.entity.Result;
import com.example.mybackend.entity.User;
import com.example.mybackend.utility.UserBack;

import java.util.List;

public interface UserService {
    public Result<User> register(User user);
    public Result<User> login(User user);
    public Result<String> logout(User user);
    public Result<User> update(User user);
    public Result<List<User>> getUsers();
    public Result<User> mostCostUser();
    public Result<User> banUser(Integer userid);
    public Result<User> unBanUser(Integer userid);
    public Result<List<MostCostUser>> usersSortedByCost(String start, String end);
}
