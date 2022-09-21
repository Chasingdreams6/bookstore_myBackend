package com.example.mybackend.dao;

import com.example.mybackend.entity.CartItem;

public interface CartItemDao {
    public void saveCartItem(CartItem cartItem);
    public void deleteCartItem(CartItem cartItem);
}
