package com.example.mybackend.daoimpl;

import com.example.mybackend.dao.BookDao;
import com.example.mybackend.entity.Book;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private BookRepository bookRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookDao.class);

    public List<Book> getBooks() {  // 获取所有的书本（简略版）
       return bookRepository.findBooks();
    }

    public Book findBookByISBN(String isbn) {
        return bookRepository.findBookById(isbn);
    }

    public Integer updateBook(Book book) {
       bookRepository.saveAndFlush(book);
       return Constants.SUCCESS;
    }

    public Integer deleteBookByISBN(String isbn) {
        bookRepository.deleteById(isbn);
        return Constants.SUCCESS;
    }

    public Integer addBook(Book book) {
        bookRepository.saveAndFlush(book);
        return Constants.SUCCESS;
    }
}
