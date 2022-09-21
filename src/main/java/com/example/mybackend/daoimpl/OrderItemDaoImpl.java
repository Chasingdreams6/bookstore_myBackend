package com.example.mybackend.daoimpl;

import com.example.mybackend.dao.OrderItemDao;
import com.example.mybackend.entity.Order;
import com.example.mybackend.entity.OrderItem;
import com.example.mybackend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderItemDaoImpl implements OrderItemDao {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrderItem(OrderItem orderItem) {
        //int x = 1 / 0;
        orderItemRepository.saveAndFlush(orderItem);
    }

    @Override
    public void deleteOrderItem(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }
}
