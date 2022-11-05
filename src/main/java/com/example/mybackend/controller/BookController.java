package com.example.mybackend.controller;

import com.example.mybackend.entity.Book;
import com.example.mybackend.utility.BookForSolr;
import com.example.mybackend.utility.Constants;
import com.example.mybackend.entity.Result;
import com.example.mybackend.service.BookService;
import com.example.mybackend.utility.Solr;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private Solr solr;

    @RequestMapping(value = "/initSolr")
    public void initSolr() throws SolrServerException, IOException {
        solr.initSolr();
    }

    @RequestMapping(value = "/fulltextSearch")
    public Result<List<BookForSolr>> search(@RequestParam("info") String s)
            throws SolrServerException, IOException {
        return solr.search(s);
    }

    @RequestMapping(value = "/getBooks" , produces="application/json;charset=UTF-8")
    public Result<List<Book>> getBooks() {return bookService.getBooks();}

    @RequestMapping(value = "/getBook", produces="application/json;charset=UTF-8")
    public Result<Book> getBook(@RequestParam("bookisbn") String isbn){
        return bookService.findBookByISBN(isbn);
    }

    @RequestMapping(value = "/updateBook")
    public Result<Book> updateBook(@RequestBody Map<String, String> params) {
        Book book = new Book(
                params.get(Constants.BOOKID),
                params.get(Constants.BOOKNAME),
                Integer.parseInt(params.get(Constants.BOOKPRICE)),
                params.get(Constants.BOOKAUTHOR),
                null,
                Integer.parseInt(params.get(Constants.BOOKREMAIN)),
                null
        );
        return bookService.updateBook(book);
    }

    @RequestMapping(value = "/deleteBookByISBN")
    public Result<Book> deleteBookByISBN(@RequestParam(Constants.BOOKID) String isbn) {
        return bookService.deleteBookByISBN(isbn);
    }

    @RequestMapping(value = "/addBook")
    public Result<Book> addBook(@RequestBody Map<String, String> params) {
        Book book = new Book(
                params.get(Constants.BOOKID),
                params.get(Constants.BOOKNAME),
                Integer.parseInt(params.get(Constants.BOOKPRICE)),
                params.get(Constants.BOOKAUTHOR),
                params.get(Constants.BOOKINFORMATIN),
                Integer.parseInt(params.get(Constants.BOOKREMAIN)),
                params.get(Constants.BOOKGUAPHURI)
        );
        return bookService.addBook(book);
    }

    @RequestMapping(value = "/updateBookISBN")
    public Result<Book> updateBookISBN(@RequestBody Map<String, String> params) {
        Book book = new Book(
                params.get(Constants.BOOKID),
                params.get(Constants.BOOKNAME),
                Integer.parseInt(params.get(Constants.BOOKPRICE)),
                params.get(Constants.BOOKAUTHOR),
                null,
                Integer.parseInt(params.get(Constants.BOOKREMAIN)),
                null
        );
        return bookService.updateBookISBN(book);
    }
}
