package com.example.mybackend.dao;


import com.example.mybackend.entity.MostCostUser;
import com.example.mybackend.entity.User;

import java.util.List;


public interface UserDao {
    public int insert(User user);
    public Integer deleteByID(Long userid);
    public Integer update(User user);
    public User SearchByName(String name);
    public User SearchByID(Integer id);
    public List<User> getUsers();
    public User mostCostUser();
    public List<MostCostUser> usersSortedByCost(String start, String end);
}
