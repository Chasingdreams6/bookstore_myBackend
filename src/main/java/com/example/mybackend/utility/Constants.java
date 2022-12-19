package com.example.mybackend.utility;

public class Constants {

    public static final String BACKEND = "http://localhost:8080";
    // 成功
    public static final int SUCCESS = 200;
    // 未知错误
    public static final int FAIL = -1;

    // TODO 后续扩展
    // 用户已经存在
    public static final int ALREADY_EXISTS = -100;
    // 用户被封禁
    public static final int USER_BANNED = -300;
    // 密码错误
    public static final int PASSWORD_ERROR = -200;

    public static final int ADMIN = 201;

    public static final int PARTIAL = 300; // 部分购买

    public static final String NUMBER = "number";
    public static final String ISBN = "isbn";
    public static final String USERID = "userid";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "userpassword";
    public static final String GENDER = "usergender";
    public static final String MAIL = "usermail";
    public static final String ADDRESS = "useraddress";
    public static final String PHONE = "userphone";
    public static final String VALIDUSER = "uservalid";


    public static final String BOOKID = "id";
    public static final String BOOKNAME = "bookname";
    public static final String BOOKPRICE = "bookprice";
    public static final String BOOKAUTHOR = "bookauthor";
    public static final String BOOKREMAIN = "bookremain";
    public static final String BOOKINFORMATIN = "bookinformation";
    public static final String BOOKGUAPHURI = "bookgraphuri";

    public static final String STARTUSERTIME = "startusertime";
    public static final String ENDUSERTIME = "endusertime";

    public static final String STARTBOOKTIME = "startbooktime";
    public static final String ENDBOOKTIME = "endbooktime";

    public static final String STARTALLORDERSTIME = "startallorderstime";
    public static final String ENDALLORDERSTIME = "endallorderstime";

    public static final String STARTORDERTIME = "startordertime";
    public static final String ENDORDERTIME = "endordertime";

    public static final String ORDERID = "orderid";
    public static final String BASE64_PREFIX = "data:image/png;base64,";
}
