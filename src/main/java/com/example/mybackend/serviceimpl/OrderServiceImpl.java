package com.example.mybackend.serviceimpl;


import com.example.mybackend.dao.CartDao;
import com.example.mybackend.dao.OrderDao;
import com.example.mybackend.entity.*;
import com.example.mybackend.service.OrderService;
import com.example.mybackend.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartDao cartDao;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private Result<Cart> result = new Result<>();
    private Result<CartItem> cartResult = new Result<>();
    private Result<List<CartItem>> cartResults = new Result<>();
    private Result<OrderItem> mResult = new Result<>();
    private Result<List<Order>> cResult = new Result<>();
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
    public Result<Cart> buyBooks(Integer userid) {
        restart();
        result.setCode(orderDao.buyBooks(userid));
        if (result.getCode() == Constants.SUCCESS) {
            result.setMsg("Success to Buy all Books");
        }
        else if (result.getCode() == Constants.PARTIAL) {
            result.setMsg("Success to buy partial books");
        }
        else result.setMsg("Fail to buy any books");
        return result;
    }
    public Result<OrderItem> buyBookByISBN(String isbn, Integer userid, Integer number) {
        mRestart();
        mResult.setCode(orderDao.buyBookByISBN(isbn, userid, number));
        if (mResult.getCode() == Constants.SUCCESS) {
            mResult.setMsg("Success to buy this book");
        }
        else mResult.setMsg("fail to buy this book");
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
        List<Order> res = orderDao.getOrders(userid);
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

    public Result<List<Order>> getOrdersByTime(Integer userid, String start, String end) {
        cRestart();
        if (userid == null) {
            cResult.setCode(Constants.FAIL);
            cResult.setMsg("Please login!");
            return cResult;
        }
        List<Order> res = orderDao.getOrdersByTime(userid, start, end);
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
}
