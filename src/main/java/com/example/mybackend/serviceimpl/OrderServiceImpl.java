package com.example.mybackend.serviceimpl;


import com.example.mybackend.dao.*;
import com.example.mybackend.entity.*;
import com.example.mybackend.service.OrderService;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.FunctionCall;
import com.example.mybackend.utility.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FunctionCall functionCall;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private Result<Cart> result = new Result<>();
    private Result<CartItem> cartResult = new Result<>();
    private Result<List<CartItem>> cartResults = new Result<>();
    private Result<OrderItem> mResult = new Result<>();
    private Result<List<Order>> cResult = new Result<>();
    private Result<Integer> sResult = new Result<>();
    private void sRestart() {
        sResult.setCode(-1);
    }
    private void restart() {
        result.setCode(-1);
        result.setMsg("");
        result.setDetail(null);
    }
    private void mRestart() {
        mResult.setCode(-1);
        mResult.setMsg("");
        mResult.setDetail(null);
    }
    private void cRestart() {
        cResult.setCode(-1);
        cResult.setMsg("");
        cResult.setDetail(null);
    }
    private void cartRestart() {
        cartResult.setCode(-1);
        cartResult.setMsg("");
        cartResult.setDetail(null);
        cartResults.setCode(-1);
        cartResults.setMsg("");
        cartResults.setDetail(null);
    }
    public Result<Cart> createCart(Integer userid) {
        restart();
        Cart res = cartDao.createCart(userid);
        if (res == null) { // fail to create a Cart, shouldn't occur
            result.setCode(Constants.FAIL);
            result.setMsg("Fail to Create a Cart");
        }
        else {
            result.setCode(Constants.SUCCESS);
            result.setMsg("Success to Create a Cart");
            result.setDetail(res);
        }
        return result;
    }
    public Result<CartItem> addCartItem(String isbn, Integer userid, Integer number) {
        cartRestart();
        cartResult.setCode(cartDao.addCartItem(isbn, userid, number));
        //User user = userDao.SearchByID(userid);
        if (cartResult.getCode() == Constants.SUCCESS) {
            cartResult.setMsg("Success to Add to Cart");
        }
        else cartResult.setMsg("Fail to Add to Cart");
        return cartResult;
    }
    public Result<CartItem> cancelCartItem(String isbn, Integer userid) {
        cartRestart();
        cartResult.setCode(cartDao.cancelBookByISBN(isbn, userid));
        if (cartResult.getCode() == Constants.SUCCESS) {
            cartResult.setMsg("Success to Cancel");
        }
        else cartResult.setMsg("Fail to Cancel");
        return cartResult;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<Cart> buyBooks(Integer userid) {
        restart();
        User user = userDao.SearchByID(userid);
        Cart cart = user.getCart();
        //   读取购物车中的所有书
        Set<CartItem> items = cart.getCartItems();
        if (items.isEmpty()) { // 无书
            result.setCode(Constants.FAIL);
            result.setMsg("Fail to buy any books");
            return result;
        }

        Order order = new Order();
        order.setUser(user);
        order.setDate(new java.sql.Date(new java.util.Date().getTime()));
        user.getOrders().add(order);

        orderDao.saveOrder(order); // 先持久化order

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
                curBook.setBookremain(last - number);
                bookDao.updateBook(curBook); // 更新book
                cartItemDao.deleteCartItem(item);  // delete from Cart
                // add new orderItems
                newItem.setOrder(order);
                order.getOrderItems().add(newItem);
                orderItemDao.saveOrderItem(newItem);
            }
            else allBuy.set(false);
        });

        orderDao.saveOrder(order);
        //session.saveOrUpdate(order);
        if (allBuy.get()) {
            result.setCode(Constants.SUCCESS);
            result.setMsg("Success to Buy all Books");
        }
        else {
            result.setMsg("Success to buy partial books");
            result.setCode(Constants.PARTIAL);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result<OrderItem> buyBookByISBN(String isbn, Integer userid, Integer number) {
        mRestart();
        Book goal = bookDao.findBookByISBN(isbn); // find this book
        Integer last = goal.getBookremain();
        User user = userDao.SearchByID(userid);
        if (last < number) { // can't buy this book
            mResult.setCode(Constants.FAIL);
            mResult.setMsg("fail to buy this book");
            return mResult;
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
        if (flag.equals(false)) {
            // 购物车中没有这本书
            mResult.setCode(Constants.FAIL);
            mResult.setMsg("fail to buy this book");
            return mResult;
        }
//        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
        Order order = new Order();
        order.setUser(user);
        order.setDate(new java.sql.Date(new java.util.Date().getTime()));

        // delete number
        goal.setBookremain(goal.getBookremain() - number);
        bookDao.updateBook(goal);  // 持久化总量
        // delete from cart
        cartItemDao.deleteCartItem(item); // 删除cartItem

        // add to Order
        OrderItem newItem = new OrderItem();        // 新建orderItem
        newItem.setBookisbn(item.getBookisbn());
        newItem.setCurprice(goal.getBookprice());
        newItem.setBooknumber(number);
        newItem.setOrder(order);
        order.getOrderItems().add(newItem); // 绑定order与orderItem
        user.getOrders().add(order);

        orderDao.saveOrder(order); // 持久化order，要在orderItem之前

        try {
            orderItemDao.saveOrderItem(newItem); // 持久化新orderItem
        }
        catch (ArithmeticException e) {
            System.out.println("handle / by zero");
        }

        mResult.setCode(Constants.SUCCESS);
        mResult.setMsg("Success to buy this book");
        return mResult;
    }
    public Result<List<CartItem>> getCarts(Integer userid) {
        cartRestart();
        if (userid == null) {
            cartResults.setCode(Constants.FAIL);
            cartResults.setMsg("please login!");
            return cartResults;
        }
        List<CartItem> res = cartDao.getCarts(userid);
        if (res == null) {
            cartResults.setCode(Constants.FAIL);
            cartResults.setMsg("Empty cart!");
        }
        else {
            cartResults.setCode(Constants.SUCCESS);
            cartResults.setMsg("Success to get Carts");
            cartResults.setDetail(res);
        }
        return cartResults;
    }
    public Result<List<Order>> getOrders(Integer userid) {
        cRestart();
        if (userid == null) {
            cResult.setCode(Constants.FAIL);
            cResult.setMsg("Please login!");
            return cResult;
        }
        User user = userDao.SearchByID(userid);
        Set<Order> orders = user.getOrders();
        List<Order> res = new ArrayList<>(orders);
        if (res == null) {
            cResult.setCode(Constants.FAIL);
            cResult.setMsg("Empty Orders!");
        } else {
            cResult.setCode(Constants.SUCCESS);
            cResult.setMsg("Success to get Orders");
            cResult.setDetail(res);
        }
        return cResult;
    }

    public Result<List<Order>> getOrdersByTime(Integer userid, String start, String end) {
        cRestart();
        if (userid == null) {
            cResult.setCode(Constants.FAIL);
            cResult.setMsg("Please login!");
            return cResult;
        }
        User user = userDao.SearchByID(userid);
        Set<Order> orders = user.getOrders();
        List<Order> res = new ArrayList<>();
        for (Order order : orders) {
            if (order.getDate().toString().compareTo(start) >= 0 &&
                    order.getDate().toString().compareTo(end) <= 0)
                res.add(order);
        }
        if (res == null) {
            cResult.setCode(Constants.FAIL);
            cResult.setMsg("Empty Orders!");
        }
        else {
            cResult.setCode(Constants.SUCCESS);
            cResult.setMsg("Success to get Orders");
            cResult.setDetail(res);
        }
        return cResult;
    }

    public Result<List<PopularBook>> popularBooks(String start, String end) {
        Result<List<PopularBook>> res = new Result<>();
        List<Order> orders = orderDao.getAllOrders();
        Map<String, Integer> cnt = new TreeMap<>();
        for (int i = 0, size = orders.size(); i < size; ++i) {
            Order order = orders.get(i);
            if (order.getDate().toString().compareTo(start) >= 0 &&
                order.getDate().toString().compareTo(end) <= 0)
            for (OrderItem item : order.getOrderItems()) {
                if (cnt.containsKey(item.getBookisbn())) {
                    Integer oldValue = cnt.get(item.getBookisbn());
                    cnt.replace(item.getBookisbn(),
                            oldValue,
                            oldValue + item.getBooknumber());
                }
                else cnt.put(item.getBookisbn(), item.getBooknumber());
            }
        }
        List<Map.Entry<String, Integer>> list
                = new ArrayList<>(cnt.entrySet());

        //降序排序
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<PopularBook> temp = new ArrayList<>();
        for (Map.Entry<String, Integer> book : list) {
            temp.add(new PopularBook(book.getKey(), book.getValue()));
        }
        res.setDetail(temp);
        res.setCode(Constants.SUCCESS);
        res.setMsg("Success to sort");
        return res;
    }

    public Result<List<Order>> allOrdersByTime(String start, String end) {
        cRestart();
        List<Order> orders = orderDao.getAllOrders();
        List<Order> res = new ArrayList<>();
        for (Order order : orders) {
            if (order.getDate().toString().compareTo(start) >= 0 &&
                order.getDate().toString().compareTo(end) <= 0)
                res.add(order);
        }
        cResult.setCode(Constants.SUCCESS);
        cResult.setMsg("success to fetch all orders by time");
        cResult.setDetail(res);
        return cResult;
    }

    public Result<Integer> sumPrice(Integer orderId) {
        sResult.setCode(Constants.SUCCESS);
        sResult.setMsg("Success to sum order price");
        List<Order> orders = orderDao.getAllOrders();
        int ans = 0;
        for (Order order : orders) {
            if (Objects.equals(order.getId(), orderId)) {
                for (OrderItem item : order.getOrderItems()) {
                    String marshalled = item.getBooknumber() + "," + item.getCurprice();
                    ans = ans + functionCall.mul(marshalled);
                    //ans = ans + item.getBooknumber() * item.getCurprice();
                }
            }
        }
        System.out.println("Success to RPC");
        sResult.setDetail(ans);
        return sResult;
    }
}
