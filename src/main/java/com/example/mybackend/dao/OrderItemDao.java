package com.example.mybackend.dao;

import com.example.mybackend.entity.Order;
import com.example.mybackend.entity.OrderItem;

public interface OrderItemDao {
    public void saveOrderItem(OrderItem orderItem);
    public void deleteOrderItem(OrderItem orderItem);
}
