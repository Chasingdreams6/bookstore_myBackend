package com.example.mybackend.dao;

import com.example.mybackend.entity.*;
import com.example.mybackend.utility.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public interface OrderDao {

    public Order createOrder(Integer userid);

    public Integer buyBookByISBN(String isbn, Integer userid, Integer number);
    public Integer buyBooks(Integer userid);
    public List<Order> getOrders(Integer userid);
    public List<Order> getOrdersByTime(Integer userid, String start, String end);
    public List<Order> getAllOrders();
}
