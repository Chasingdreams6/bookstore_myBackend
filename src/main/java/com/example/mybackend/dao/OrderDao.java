package com.example.mybackend.dao;

import com.example.mybackend.entity.*;
import java.util.List;

public interface OrderDao {

    public List<Order> getAllOrders();
    public void saveOrder(Order order);
}
