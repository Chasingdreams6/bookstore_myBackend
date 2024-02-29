package com.example.mybackend.utility;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

public class BookForSolr implements Serializable {
    @Field("bookname")
    public String bookName;

    @Field("bookinformation")
    public String bookInformation;

    @Field("bookauthor")
    public String bookAuthor;

    @Field("bookisbn")
    public String bookIsbn;

    public BookForSolr(String isbn, String name, String author, String info) {
        bookIsbn = isbn;
        bookAuthor = author;
        bookName = name;
        bookInformation = info;
    }

    public BookForSolr() {}

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookInformation() {
        return bookInformation;
    }

    public void setBookInformation(String bookInformation) {
        this.bookInformation = bookInformation;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }
}
