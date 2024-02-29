package com.example.mybackend.utility;

import com.example.mybackend.dao.BookDao;
import com.example.mybackend.entity.Book;
import com.example.mybackend.entity.Result;
import com.example.mybackend.service.BookService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Solr {

    @Autowired
    private BookDao bookDao;

    public Solr() {}

    public SolrClient getSolrClient() {
        final String url = "http://localhost:8983/solr/bookstore";
        HttpSolrClient client = new HttpSolrClient.Builder(url)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        System.out.println(client);
        return client;
    }
    public void initSolr() throws SolrServerException, IOException { // flush and init
        List<Book> books = bookDao.getBooks();
        SolrClient client = getSolrClient();
        client.deleteByQuery("*:*");
        for (Book book : books) {
            BookForSolr bookForSolr = new BookForSolr(book.getId()
                    , book.getBookname()
                    , book.getBookauthor()
                    , book.getBookinformation());
            client.addBean(bookForSolr);
        }
        client.commit();
    }
    public Result<List<BookForSolr>> search(String s) throws SolrServerException, IOException {
        List<BookForSolr> res = new ArrayList<>();
        SolrClient client = getSolrClient();
        SolrQuery query = new SolrQuery();
        query.set("q", s);
        query.set("df", "bookinformation");
        query.set("rows", "999");
        QueryResponse response = client.query(query);
        SolrDocumentList docs = response.getResults();
        for (SolrDocument doc : docs) {
            BookForSolr cur = new BookForSolr((String) doc.get("bookisbn"),
                    (String)doc.get("bookname"),
                    (String)doc.get("bookauthor"),
                    (String)doc.get("bookinformation"));
            //System.out.println("isbn:" + doc.get("bookisbn") + "name:" + doc.get("bookname"));
            res.add(cur);
        }
        return new Result<>(Constants.SUCCESS, "success to search", res);
    }
}
