package com.example.mybackend.daoimpl;


import com.example.mybackend.dao.BookDao;
import com.example.mybackend.dao.OrderDao;
import com.example.mybackend.dao.UserDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.repository.OrderRepository;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    BookDao bookDao;
    @Autowired
    UserDao userDao;

    @Autowired
    OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);

//    public Order findOrderByUserID(Integer userid) {
//        logger.info("try to find a Order...");
//        List<Cart> orders = session.createQuery("from Cart where userid=:userid")
//                .setParameter("userid", userid).list();
//        if (orders.isEmpty()) return null;
//        return orders.get(0);
//    }


    public Order createOrder(Integer userid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        User user = userDao.SearchByID(userid);
        session.beginTransaction();
        Order order = new Order();
        order.setUser(user);
        user.getOrders().add(order);
        session.save(order);
        session.getTransaction().commit();
        return order;
    }


    public Integer buyBookByISBN(String isbn, Integer userid, Integer number) { // 某人确定购买某本书
        Book goal = bookDao.findBookByISBN(isbn); // find this book
        //System.out.println(goal.toString());
        Integer last = goal.getBookremain();
        User user = userDao.SearchByID(userid);
        if (last < number) { // can't buy this book
            return Constants.FAIL;
        }
        Cart cart = user.getCart();
        Set<CartItem> items = cart.getCartItems();
        CartItem item = new CartItem();
        Boolean flag = false;
        for (CartItem it : items) {
            if (it.getBookisbn().equals(isbn)) {
                item = it;
                flag = true;
            }
        }
        if (flag.equals(false)) return Constants.FAIL; // 购物车中没有这本书
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Order order = new Order();
        order.setUser(user);
        order.setDate(new java.sql.Date(new java.util.Date().getTime()));
        user.getOrders().add(order);
        session.save(order);
        // delete number
        goal.setBookremain(goal.getBookremain() - number);
        session.update(goal);
        // delete from cart
        session.delete(item);
        // add to Order
        OrderItem newItem = new OrderItem();
        newItem.setBookisbn(item.getBookisbn());
        newItem.setCurprice(goal.getBookprice());
        newItem.setBooknumber(number);
        newItem.setOrder(order);
        order.getOrderItems().add(newItem);

        session.saveOrUpdate(order);
        session.save(newItem);
        session.getTransaction().commit();
        return Constants.SUCCESS;
    }
    public Integer buyBooks(Integer userid) { // 某人买他购物车内的所有书

        User user = userDao.SearchByID(userid);
        Cart cart = user.getCart();
        //   读取购物车中的所有书
        Set<CartItem> items = cart.getCartItems();
        if (items.isEmpty()) return Constants.FAIL; // 无书

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Order order = new Order();
        order.setUser(user);
        order.setDate(new java.sql.Date(new java.util.Date().getTime()));
        user.getOrders().add(order);
        session.save(order);
        AtomicReference<Boolean> allBuy = new AtomicReference<>(true);

        items.forEach(item->{
            Book curBook = bookDao.findBookByISBN(item.getBookisbn());
            Integer last = curBook.getBookremain();
            Integer number = item.getBooknumber();

            if (last >= number) {
                OrderItem newItem = new OrderItem();
                newItem.setCurprice(curBook.getBookprice());
                newItem.setBooknumber(number);
                newItem.setBookisbn(curBook.getId());

                // delete from books
                curBook.setBookremain(last - number);
                session.update(curBook);
                // delete from Cart
                session.delete(item);
                // add new orderItems
                newItem.setOrder(order);
                order.getOrderItems().add(newItem);
                session.save(newItem);
            }
            else allBuy.set(false);
        });

        session.saveOrUpdate(order);
        session.getTransaction().commit();
        if (allBuy.get())
            return Constants.SUCCESS;
        else return Constants.PARTIAL;
    }


    public List<Order> getOrders(Integer userid) { // 获取一个人的所有orders
        User user = userDao.SearchByID(userid);
        Set<Order> orders = user.getOrders();
        return new ArrayList<>(orders);
    }

    @Override
    public List<Order> getOrdersByTime(Integer userid, String start, String end) {
        User user = userDao.SearchByID(userid);
        Set<Order> orders = user.getOrders();
        List<Order> res = new ArrayList<>();
        for (Order order : orders) {
            if (order.getDate().toString().compareTo(start) >= 0 &&
                order.getDate().toString().compareTo(end) <= 0)
                res.add(order);
        }
        return res;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
