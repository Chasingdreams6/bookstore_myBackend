package com.example.mybackend.utility;

import javax.servlet.http.HttpSession;

public class OrderItemSession {
    private String isbn;
    private String userid;
    private String number;
    private Integer code;
    private String msg;
    private HttpSession session;

    public OrderItemSession() {}

    public OrderItemSession(String isbn, String userid, String number, HttpSession session) {
        this.isbn = isbn;
        this.userid = userid;
        this.number = number;
        this.session = session;
    }

    OrderItemSession(Integer code, String msg, String userid) {
        this.code = code;
        this.msg = msg;
        this.userid = userid;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
