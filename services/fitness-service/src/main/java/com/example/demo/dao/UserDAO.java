package com.example.demo.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.example.demo.model.User;

@Repository
public interface UserDAO extends GenericDAO<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
