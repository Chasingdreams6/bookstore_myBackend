package com.example.mybackend.controller;

import com.example.mybackend.entity.*;
import com.example.mybackend.service.OrderService;
import com.example.mybackend.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    private OrderService cartService;

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
    public Result<Cart> doBuy(@RequestBody Map<String, String> params) {
        return cartService.buyBooks(Integer.parseInt(params.get(Constants.USERID)));
    }

    @RequestMapping("/buyBook")
    public Result<OrderItem> buyOne(@RequestBody Map<String, String> params) {
        return cartService.buyBookByISBN(
                params.get(Constants.ISBN),
                Integer.parseInt(params.get(Constants.USERID)),
                Integer.parseInt(params.get(Constants.NUMBER)));
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
}
