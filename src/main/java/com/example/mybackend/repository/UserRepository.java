package com.example.mybackend.repository;

import com.example.mybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(Integer userid);
    User findUserByUsername(String name);
}
