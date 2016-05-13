package com.example.dao;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByName(String name);
    User findById(int id);

    User findByMailAndPassword(String mail, String password);
    User findByNameAndPassword(String name, String password);
}
