package com.example.demo.dao;

import java.util.Optional;
import com.example.demo.model.User;

public interface UserDAO extends GenericDAO<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
