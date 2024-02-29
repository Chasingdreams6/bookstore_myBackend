package com.example.mybackend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "orderitems")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler", "order"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitemid", nullable = false)
    private Integer id;

    @Column(name = "booknumber", nullable = false)
    private Integer booknumber;

    @Column(name = "bookisbn", nullable = false)
    private String bookisbn;

    @Column(name = "curprice", nullable = false)
    private Integer curprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private Order order = new Order();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBooknumber() {
        return booknumber;
    }

    public void setBooknumber(Integer booknumber) {
        this.booknumber = booknumber;
    }

    public Integer getCurprice() {
        return curprice;
    }

    public void setCurprice(Integer curprice) {
        this.curprice = curprice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getBookisbn() {
        return bookisbn;
    }

    public void setBookisbn(String bookisbn) {
        this.bookisbn = bookisbn;
    }

    @Override
    public String toString() {
        return id + "," + booknumber + "," + bookisbn + "," + curprice;
    }
}
