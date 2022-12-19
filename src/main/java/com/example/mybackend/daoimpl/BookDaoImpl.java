package com.example.mybackend.daoimpl;

import com.alibaba.fastjson.JSONArray;
import com.example.mybackend.dao.BookDao;
import com.example.mybackend.entity.Book;
import com.example.mybackend.entity.BookIcon;
import com.example.mybackend.repository.BookIconRepository;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.repository.BookRepository;
import com.example.mybackend.utility.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookIconRepository bookIconRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookDao.class);
    private final String allBookKey = "BookDao_Books";

    @Autowired
    private RedisUtil redisUtil;
    public List<Book> getBooks() {
        List<Book> resBook = null;
        Object res = redisUtil.get(allBookKey);
        if (res == null) {
            System.out.println("getBooks: not in redis");
            resBook = bookRepository.findBooks();
            for (int i = 0; i < resBook.size(); ++i) {
                Optional<BookIcon> image = bookIconRepository.findById(resBook.get(i).getId());
                if (image.isPresent())
                    resBook.get(i).setBookIcon(image.get());
            }
            redisUtil.set(allBookKey, JSONArray.toJSON(resBook));
        } else {
            System.out.println("getBooks: in redis");
            resBook = JSONArray.parseArray(res.toString(), Book.class);
        }
        return resBook;
    }

    public Book findBookByISBN(String isbn) {
        String key = "Book_" + isbn;
        Book res = null;
        Object cache = redisUtil.get(key);
        if (cache == null) {
            System.out.println("findBookByISBN: not in redis " + "isbn: " + isbn);
            res = bookRepository.findBookById(isbn);
            res.setBookIcon(bookIconRepository.findById(res.getId()).get());
            if (res != null)
                redisUtil.set(key, JSONArray.toJSON(res));
        }  else {
            System.out.println("findBookByISBN: in redis "
                    + "isbn: " + isbn + " str: " + cache.toString());
            res =  JSONArray.parseObject(cache.toString(), Book.class);
        }
        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateBook(Book book) {
        String key = "Book_" + book.getId();
        System.out.println("updateBook: update redis " + "isbn: " + book.getId());
        redisUtil.set(key, JSONArray.toJSON(book));
        redisUtil.del(allBookKey);
        bookRepository.saveAndFlush(book);
        bookIconRepository.save(book.getBookIcon());
        return Constants.SUCCESS;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer deleteBookByISBN(String isbn) {
        String key = "Book_" + isbn;
        System.out.println("deleteBookByISBN: delete redis " + "isbn: " + isbn);
        redisUtil.del(key);
        redisUtil.del(allBookKey);
        bookRepository.deleteById(isbn);
        bookIconRepository.deleteById(isbn);
        return Constants.SUCCESS;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addBook(Book book) {
        String key = "Book_" + book.getId();
        System.out.println("addBook: add in redis " + "isbn: " + book.getId());
        redisUtil.set(key, JSONArray.toJSON(key));
        redisUtil.del(allBookKey);
        bookRepository.saveAndFlush(book);
        bookIconRepository.save(book.getBookIcon());
        return Constants.SUCCESS;
    }
}
