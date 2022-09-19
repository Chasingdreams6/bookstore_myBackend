package com.example.mybackend.serviceimpl;

import com.example.mybackend.dao.BookDao;
import com.example.mybackend.entity.Book;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.entity.Result;
import com.example.mybackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;
    private Result<List<Book>> result = new Result<>();
    private Result<Book> mresult = new Result<>();

    public Result<List<Book>> getBooks() { // 将books转换为json字符串
        List<Book> books = bookDao.getBooks();
        if (books.isEmpty()) {
            result.setCode(Constants.FAIL);
            result.setMsg("Empty database");
            return result;
        }
        result.setDetail(books);
        result.setCode(Constants.SUCCESS);
        result.setMsg("Success to fetch");
        return result;
    }

    public Result<Book> findBookByISBN(String isbn) {
        Book book = bookDao.findBookByISBN(isbn);

        if (book == null) {
            mresult.setCode(Constants.FAIL);
            mresult.setMsg("Not found this book");
            mresult.setDetail(null);
        }
        else {
            mresult.setCode(Constants.SUCCESS);
            mresult.setMsg("Found this book");
            mresult.setDetail(book);
        }
        return mresult;
    }

    public Result<Book> updateBook(Book book) {
        Book lastBook = bookDao.findBookByISBN(book.getId());
        if (lastBook == null) {
            mresult.setCode(Constants.FAIL);
            mresult.setMsg("Book doesn't exist!");
            mresult.setDetail(null);
        }
        else {
            book.setBookgraphuri(lastBook.getBookgraphuri());
            book.setBookinformation(lastBook.getBookinformation());
            if (bookDao.updateBook(book) > 0) {
                mresult.setCode(Constants.SUCCESS);
                mresult.setMsg("success to update book");
                mresult.setDetail(book);
            }
            else {
                mresult.setCode(Constants.FAIL);
                mresult.setMsg("Update Book Error!");
                mresult.setDetail(null);
            }
        }
        return mresult;
    }

    public Result<Book> deleteBookByISBN(String isbn) {
        Integer res = bookDao.deleteBookByISBN(isbn);
        if (res > 0) {
            mresult.setCode(Constants.SUCCESS);
            mresult.setMsg("Success to delete a book");
            mresult.setDetail(null);
        }
        else {
            mresult.setCode(Constants.FAIL);
            mresult.setMsg("delete book error");
            mresult.setDetail(null);
        }
        return mresult;
    }

    public Result<Book> addBook(Book book) {
        Book flag = bookDao.findBookByISBN(book.getId());
        if (flag != null) {
            mresult.setCode(Constants.FAIL);
            mresult.setMsg("the book isbn has been existed!");
            mresult.setDetail(null);
            return mresult;
        }
        Integer res = bookDao.addBook(book);
        if (res > 0) {
            mresult.setCode(Constants.SUCCESS);
            mresult.setMsg("Success to add a book");
            mresult.setDetail(null);
        }
        else {
            mresult.setCode(Constants.FAIL);
            mresult.setMsg("add book error");
            mresult.setDetail(null);
        }
        return mresult;
    }


    public Result<Book> updateBookISBN(Book book) {
        Book lastBook = bookDao.findBookByISBN(book.getId());
        if (lastBook == null) {
            mresult.setCode(Constants.SUCCESS);
            mresult.setMsg("Success to update ISBN");
            mresult.setDetail(book);
            addBook(book);
        }
        else {
            mresult.setCode(Constants.FAIL);
            mresult.setMsg("The ISBN has been existed!");
            mresult.setDetail(null);
        }
        return mresult;
    }

}
