package com.example.mybackend.utility;

public class ItemBack {
    public Integer id;
    public Integer orderbooknumber;
    public String bookisbn;
    public Integer curprice;
    public ItemBack(Integer id, Integer number,
                    String isbn, Integer curprice) {
        this.bookisbn = isbn;
        this.id = id;
        this.orderbooknumber = number;
        this.curprice = curprice;
    }
}
