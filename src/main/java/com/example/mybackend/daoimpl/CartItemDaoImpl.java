package com.example.mybackend.daoimpl;

import com.example.mybackend.dao.CartItemDao;
import com.example.mybackend.entity.CartItem;
import com.example.mybackend.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CartItemDaoImpl implements CartItemDao {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }
}
