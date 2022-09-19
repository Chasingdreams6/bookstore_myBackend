package com.example.mybackend.repository;

import com.example.mybackend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    @Query("from Book")
    List<Book> findBooks();

    Book findBookById(String isbn);

}
