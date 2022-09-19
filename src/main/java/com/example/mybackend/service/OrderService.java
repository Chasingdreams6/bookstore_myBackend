package com.example.mybackend.service;

import com.example.mybackend.entity.*;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface OrderService {
    public Result<Cart> createCart(Integer userid);
    public Result<CartItem> addCartItem(String isbn, Integer userid, Integer number);
    public Result<CartItem> cancelCartItem(String isbn, Integer userid);
    public Result<Cart> buyBooks(Integer userid);
    public Result<OrderItem> buyBookByISBN(String isbn, Integer userid, Integer number);
    public Result<List<CartItem>> getCarts(Integer userid);
    public Result<List<Order>> getOrders(Integer userid);
    public Result<List<Order>> getOrdersByTime(Integer userid, String start, String end);
    public Result<List<PopularBook>> popularBooks(String start, String end);
    public Result<List<Order>> allOrdersByTime(String start, String end);
}
