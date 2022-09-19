package com.example.mybackend.dao;


import com.example.mybackend.entity.Book;

import java.util.List;

public interface BookDao {
    public List<Book> getBooks();
    public Book findBookByISBN(String isbn);
    public Integer updateBook(Book book);
    public Integer deleteBookByISBN(String isbn); // >0 success, <0 fail
    public Integer addBook(Book book);
}
