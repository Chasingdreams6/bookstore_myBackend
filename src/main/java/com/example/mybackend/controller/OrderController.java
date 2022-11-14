package com.example.mybackend.controller;

import com.example.mybackend.entity.*;
import com.example.mybackend.service.OrderService;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.utility.OrderItemSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    private OrderService cartService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    HttpServletRequest request;

    @RequestMapping("createCart")
    public Result<Cart> createCart(@RequestParam(Constants.USERID) Integer userid) {
        return cartService.createCart(userid);
    }

    @RequestMapping("/addItem")
    public Result<CartItem> doAddItem(@RequestBody Map<String, String> params) {
       return cartService.addCartItem(
               params.get(Constants.ISBN),
               Integer.parseInt(params.get(Constants.USERID)),
               Integer.parseInt(params.get(Constants.NUMBER)));
    }

    @RequestMapping("/cancelItem")
    public Result<CartItem> doCancelItem(@RequestBody Map<String, String> params) {
        return cartService.cancelCartItem(
                params.get(Constants.ISBN),
                Integer.parseInt(params.get(Constants.USERID))
        );
    }
    /*
    * @params: userid
    * @effect: buy all books in his cart
    * */
    @RequestMapping("/buyBooks")
    public void doBuy(@RequestBody Map<String, String> params) {
        kafkaTemplate.send("buyAllTopic", "key", params.get(Constants.USERID));
    }

    @RequestMapping("/buyBook")
    public void buyOne(@RequestBody Map<String, String> params) {
        String message = (String) params.get(Constants.ISBN) + ","
                + (String) params.get(Constants.USERID) + ","
                + (String) params.get(Constants.NUMBER);
        kafkaTemplate.send("buyTopic", "key", message);
    }

    @RequestMapping("/getCarts")
    public Result<List<CartItem>> getCarts(@RequestBody Map<String, String> params) {
        try {
            String ids = params.get(Constants.USERID);
            if (ids == null) return cartService.getCarts(null);
            return cartService.getCarts(Integer.parseInt(ids));
        } catch (NullPointerException e) {
            return cartService.getCarts(null);
        }
    }

    @RequestMapping("/getOrders")
    public Result<List<Order>> getOrders(@RequestBody Map<String, String> params) {
        try {
            String ids = params.get(Constants.USERID);
            return cartService.getOrders(Integer.parseInt(ids));
        } catch (NullPointerException e) {
            return cartService.getOrders(null);
        }
    }

    @RequestMapping("/getOrdersByTime")
    public Result<List<Order>> getOrdersByTime(@RequestBody Map<String, String> params) {
        try {
            String ids = params.get(Constants.USERID);
            String start = params.get(Constants.STARTORDERTIME);
            String end = params.get(Constants.ENDORDERTIME);
            if (ids == null) return cartService.getOrders(null);
            return cartService.getOrdersByTime(Integer.parseInt(ids), start, end);
        } catch (NullPointerException e) {
            return cartService.getOrders(null);
        }
    }

    @RequestMapping(value = "/popularBooks")
    public Result<List<PopularBook>> popularBooks(
            @RequestParam(Constants.STARTBOOKTIME) String start,
            @RequestParam(Constants.ENDBOOKTIME) String end) {
        return cartService.popularBooks(start, end);
    }

    @RequestMapping(value = "/allOrdersByTime")
    public Result<List<Order>> allOrdersByTime(
            @RequestParam(Constants.STARTALLORDERSTIME) String start,
            @RequestParam(Constants.ENDALLORDERSTIME) String end) {
        return cartService.allOrdersByTime(start, end);
    }

    @RequestMapping(value = "/sumPrice")
    public Result<Integer> sumPrice(@RequestParam(Constants.ORDERID) Integer orderId) {
        return cartService.sumPrice(orderId);
    }
}
