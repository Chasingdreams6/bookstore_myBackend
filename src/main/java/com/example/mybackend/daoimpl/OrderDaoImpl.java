package com.example.mybackend.daoimpl;


import com.alibaba.fastjson.JSONArray;
import com.example.mybackend.dao.BookDao;
import com.example.mybackend.dao.OrderDao;
import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.repository.OrderRepository;
import com.example.mybackend.utility.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    BookDao bookDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RedisUtil redisUtil;
    private String allOrdersKey = "OrderDao_allOrders";

    public List<Order> getAllOrders() {
        Object cache = redisUtil.get(allOrdersKey);
        List<Order> res = null;
        if (cache == null) {
            System.out.println("getAllOrders: not in redis");
            res = orderRepository.findAll();
            redisUtil.set(allOrdersKey, JSONArray.toJSON(res));
        } else {
            System.out.println("getAllOrders: in redis");
            res = JSONArray.parseArray(cache.toString(), Order.class);
        }
        return res;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrder(Order order) {
        System.out.println("saveOrder: clean all orders cache in redis");
        redisUtil.del(allOrdersKey);
        orderRepository.saveAndFlush(order);
    }
}
