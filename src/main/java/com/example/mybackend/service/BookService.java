package com.example.mybackend.service;

import com.example.mybackend.entity.Book;
import com.example.mybackend.entity.PopularBook;
import com.example.mybackend.entity.Result;
import java.util.List;


public interface BookService {
    public Result<List<Book>> getBooks();
    public Result<Book> findBookByISBN(String isbn);
    public Result<Book> updateBook(Book book);
    public Result<Book> deleteBookByISBN(String isbn);
    public Result<Book> addBook(Book book);
    public Result<Book> updateBookISBN(Book book);
}
