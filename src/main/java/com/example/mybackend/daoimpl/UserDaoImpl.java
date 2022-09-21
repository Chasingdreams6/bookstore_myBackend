package com.example.mybackend.daoimpl;

import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.repository.UserRepository;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository     // 数据库访问类
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    /*
     * add a user
     * @param user
     * @return lineNumber that effected, >= 1 is success
     * */
    public int insert(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        int res = (int) session.save(user);
        session.getTransaction().commit();
        session.close();
        return res;
    }

    // delete a user
    public void deleteByID(int userid) {
        userRepository.deleteById(userid);
    }

    // update a user
    public Integer update(User user) {
        userRepository.saveAndFlush(user);
        return Constants.SUCCESS;
    }

    public User SearchByName(String name) {
        return userRepository.findUserByUsername(name);
    }

    public User SearchByID(Integer id) {
        return userRepository.findUserById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
