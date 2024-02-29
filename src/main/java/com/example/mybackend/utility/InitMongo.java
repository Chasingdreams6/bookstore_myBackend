package com.example.mybackend.utility;

import com.example.mybackend.entity.Book;
import com.example.mybackend.entity.BookIcon;
import com.example.mybackend.repository.BookIconRepository;
import com.example.mybackend.repository.BookRepository;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class InitMongo {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookIconRepository bookIconRepository;

    public InitMongo() {}

    public void initMongo() {
        List<Book> books = bookRepository.findBooks();
        bookIconRepository.deleteAll();
        for (Book book : books) {
            BookIcon icon = new BookIcon(book.getId(), book.getBookgraphuri());
            icon.setIconBase64(Base64Util.imageUrlToBase64(book.getBookgraphuri()));
            bookIconRepository.save(icon);
        }
        //test
        BookIcon icon = bookIconRepository.findById(books.get(0).getId()).get();
        if (icon != null) {
            //System.out.println(icon.getIconBase64());
        }
        else {
           // System.out.println("error");
        }
    }
}
