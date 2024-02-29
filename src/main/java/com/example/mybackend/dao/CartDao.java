package com.example.mybackend.dao;

import com.example.mybackend.entity.Cart;
import com.example.mybackend.entity.CartItem;
import com.example.mybackend.entity.OrderItem;

import java.util.List;

public interface CartDao {
    public Integer addCartItem(String isbn, Integer userid, Integer number);
    public Cart createCart(Integer userid);

    public CartItem findCartItemByISBN(String isbn, Integer userid);

    public Integer getCartID(Integer userid);

    public Integer cancelBookByISBN(String isbn, Integer userid);
    public List<CartItem> getCarts(Integer userid);

    public Cart findCartByCartID(Integer cartid);
    public void saveCart(Cart cart);
}
