package com.example.mybackend.daoimpl;

import com.alibaba.fastjson.JSONArray;
import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.HibernateUtil;
import com.example.mybackend.utility.RedisUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository     // 数据库访问类
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisUtil redisUtil;

    private final String allUsersKey = "UserDao_users";

    /*
     * add a user
     * @param user
     * @return lineNumber that effected, >= 1 is success
     * */
    public int insert(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        System.out.println("insert: clean all users in redis");
        redisUtil.del(allUsersKey);
        int res = (int) session.save(user);
        session.getTransaction().commit();
        session.close();
        return res;
    }

    // delete a user
    public void deleteByID(int userid) {
        String id_key = "UserDao_user_id_" + userid;
        System.out.println("deleteByID: clean redis");
        redisUtil.del(id_key);
        redisUtil.del(allUsersKey);
        userRepository.deleteById(userid);
    }

    // update a user, assume userid not change
    public Integer update(User user) {
        User oldUser = SearchByID(user.getId());
        String name_key = "UserDao_user_name_" + oldUser.getUsername();
        String id_key = "UserDao_user_id_" + oldUser.getId();
        System.out.println("update: clean redis");
        redisUtil.del(name_key);
        redisUtil.del(id_key);
        redisUtil.del(allUsersKey);
        System.out.println("update: update redis");
        name_key = "UserDao_user_name_" + user.getUsername();
        id_key = "UserDao_user_id_" + user.getId();
        redisUtil.set(name_key, JSONArray.toJSON(user));
        redisUtil.set(id_key, JSONArray.toJSON(user));
        userRepository.saveAndFlush(user);
        return Constants.SUCCESS;
    }

    public User SearchByName(String name) {
        String key = "UserDao_user_name_" + name;
        Object cache = redisUtil.get(key);
        User res;
        if (cache == null) {
            System.out.println("getUserByName: not in redis " + "name: " + name);
            res = userRepository.findUserByUsername(name);
            if (res != null)
                redisUtil.set(key, JSONArray.toJSON(res));
        } else {
            System.out.println("getUserByName: in redis " + "name: " + name);
            res = JSONArray.parseObject(cache.toString(), User.class);
        }
        return res;
    }

    public User SearchByID(Integer id) {
        String key = "UserDao_user_id_" + id;
        Object cache = redisUtil.get(key);
        User res;
        if (cache == null) {
            System.out.println("getUserByID: not in redis " + "id: " + id);
            res = userRepository.findUserById(id);
            if (res != null)
                redisUtil.set(key, JSONArray.toJSON(res));
        } else {
            System.out.println("getUserByID: in redis " + "id: " + id);
            res = JSONArray.parseObject(cache.toString(), User.class);
        }
        return res;
    }

    public List<User> getUsers() {
        Object cache = redisUtil.get(allUsersKey);
        List<User> res = null;
        if (cache == null) {
            System.out.println("getUsers: not in redis");
            res = userRepository.findAll();
            redisUtil.set(allUsersKey, JSONArray.toJSON(res));
        } else {
            System.out.println("getUsers: in redis");
            res = JSONArray.parseArray(cache.toString(), User.class);
        }
        return res;
    }

}
