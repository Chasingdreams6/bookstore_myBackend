package com.example.mybackend.daoimpl;


import com.example.mybackend.dao.BookDao;
import com.example.mybackend.dao.OrderDao;
import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.repository.OrderRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrder(Order order) {
        orderRepository.saveAndFlush(order);
    }
}
