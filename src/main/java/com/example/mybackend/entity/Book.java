package com.example.mybackend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Book {
    @Id
    @Column(name = "bookisbn", nullable = false, length = 20)
    private String id;

    @Column(name = "bookname", nullable = false, length = 50)
    private String bookname;

    @Column(name = "bookprice", nullable = false)
    private Integer bookprice;

    @Column(name = "bookauthor", nullable = false, length = 20)
    private String bookauthor;

    @Column(name = "bookinformation", length = 100)
    private String bookinformation;

    @Column(name = "bookremain")
    private Integer bookremain;

    @Column(name = "bookgraphuri", length = 50)
    private String bookgraphuri;

    public Book() {}

    public Book(String isbn, String name, Integer price, String author, String info, Integer remain,
                String uri) {
        this.id = isbn;
        this.bookname = name;
        this.bookprice = price;
        this.bookauthor = author;
        this.bookinformation = info;
        this.bookremain = remain;
        this.bookgraphuri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public Integer getBookprice() {
        return bookprice;
    }

    public void setBookprice(Integer bookprice) {
        this.bookprice = bookprice;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookinformation() {
        return bookinformation;
    }

    public void setBookinformation(String bookinformation) {
        this.bookinformation = bookinformation;
    }

    public Integer getBookremain() {
        return bookremain;
    }

    public void setBookremain(Integer bookremain) {
        this.bookremain = bookremain;
    }

    public String getBookgraphuri() {
        return bookgraphuri;
    }

    public void setBookgraphuri(String bookgraphuri) {
        this.bookgraphuri = bookgraphuri;
    }

    public String toString() {
        return String.format("book[bookisbn=%s, bookprice=%d, bookname=%s]\n",
                id, bookprice, bookname);
    }
}
