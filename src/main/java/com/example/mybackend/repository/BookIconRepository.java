package com.example.mybackend.repository;

import com.example.mybackend.entity.BookIcon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookIconRepository extends MongoRepository<BookIcon, String> {
    @Override
    List<BookIcon> findAll();
}
