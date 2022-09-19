package com.example.mybackend.daoimpl;

import com.example.mybackend.dao.BookDao;
import com.example.mybackend.dao.CartDao;
import com.example.mybackend.dao.OrderDao;
import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class CartDaoImpl implements CartDao {
    @Autowired
    BookDao bookDao;
    @Autowired
    UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);

    public Integer getCartID(Integer userid) { // 获取一个人的CartID
        User user = userDao.SearchByID(userid);
        Cart cart = user.getCart();
        return cart.getId();
    }

    public Cart findCartByCartID(Integer cartid) {
        logger.info("try to find a order...");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Cart> carts;
        try {
            carts = session.createQuery("from Cart where id=:orderid")
                    .setParameter("orderid",cartid).list();
        } catch (EmptyResultDataAccessException e) {
            session.close();
            return null;
        }
        session.close();
        if (carts.isEmpty()) return null;
        return carts.get(0);
    }
    public Cart createCart(Integer userid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        User user = userDao.SearchByID(userid);
        session.beginTransaction();
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        session.save(cart);
        session.getTransaction().commit();
        return cart;
    }
    public CartItem findCartItemByISBN(String isbn, Integer userid) { // 查询某个人购物车中某本书
        logger.info("查询某个人购物车中某本书...");
        User user = userDao.SearchByID(userid);
        Cart cart = user.getCart();
        Set<CartItem> items = cart.getCartItems();
        for (CartItem item: items) {
            if (item.getBookisbn().equals(isbn)) return item;
        }
        return null;
    }
    public Integer addCartItem(String isbn, Integer userid, Integer number) { // 某人添加一本书到自己的购物车
        //System.out.println(userid);
        User user = userDao.SearchByID(userid);
        System.out.println(user.getUsername());
        CartItem cartItem = findCartItemByISBN(isbn, userid); // 检查购物车是否有这本书
        Book goal = bookDao.findBookByISBN(isbn);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        if (cartItem == null) { // 新加
            System.out.println("new add book to cart");
            if (number > goal.getBookremain()) return Constants.FAIL;
            CartItem item = new CartItem();
            item.setBooknumber(number);
            item.setBookisbn(isbn);
            item.setCart(user.getCart());
            user.getCart().getCartItems().add(item);
            session.save(item);
        }
        else { // 与之前的购物车合并
            System.out.println("merge book to cart");
            if (number + cartItem.getBooknumber() > goal.getBookremain()) return Constants.FAIL;
            cartItem.setBooknumber(number + cartItem.getBooknumber());
            session.update(cartItem);
        }
        session.getTransaction().commit();
        return Constants.SUCCESS;
    }

    public Integer cancelBookByISBN(String isbn, Integer userid) { // 某人从购物车中取消一本书
        User user = userDao.SearchByID(userid);
        Cart cart = user.getCart();
        Set<CartItem> items = cart.getCartItems();
        boolean flag = false;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        for (CartItem item : items) {
            if (item.getBookisbn().equals(isbn)) {
                session.delete(item);
                flag = true;
                break;
            }
        }
        session.getTransaction().commit();
        if (!flag) return Constants.FAIL;
        return Constants.SUCCESS;
    }

    public List<CartItem> getCarts(Integer userid) { // 获取Cart的全部信息
        User user = userDao.SearchByID(userid);
        Cart cart = user.getCart();
        List<CartItem> res = new ArrayList<>(cart.getCartItems());
        return res;
    }
}
