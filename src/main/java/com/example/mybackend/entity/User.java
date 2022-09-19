package com.example.mybackend.entity;

import com.example.mybackend.utility.UserBack;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "userid", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "userpassword", nullable = false, length = 20)
    private String userpassword;

    @Column(name = "usergender", length = 10)
    private String usergender;

    @Column(name = "usermail", length = 30)
    private String usermail;

    @Column(name = "useraddress", length = 50)
    private String useraddress;

    @Column(name = "userphone", length = 11)
    private String userphone;

    @Column(name = "uservalid")
    private Integer valid;
//    @OneToMany(mappedBy = "user2", cascade = CascadeType.REMOVE,
//            fetch = FetchType.EAGER, targetEntity = Order.class)
//    private Set<Order> orders = new LinkedHashSet<>();
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, targetEntity = Cart.class,
//    fetch = FetchType.EAGER)
//    private Cart cart = new Cart();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Cart cart = new Cart();
    public User() {}

    public User(Integer id, String name, String password,
                String gender, String mail, String address,
                String phone, Integer valid) {
        this.id = id;
        this.username = name;
        this.userpassword = password;
        this.usergender = gender;
        this.usermail = mail;
        this.useraddress = address;
        this.userphone = phone;
        this.valid = valid;
    }
    public String toString() {
        return String.format("id:%d,username:%s,password:%s", id, username, userpassword);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }


    public Cart getCart(){return cart;}

    public void setCart(Cart cart) {this.cart = cart;}


    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getCostByTime(String start, String end) {
        int curCost = 0;
        for (Order order : orders) {
            if (order.getDate().toString().compareTo(start) >= 0 &&
                order.getDate().toString().compareTo(end) <= 0)
            for (OrderItem item : order.getOrderItems()) {
                curCost += item.getBooknumber() * item.getCurprice();
            }
        }
        return curCost;
    }
}
