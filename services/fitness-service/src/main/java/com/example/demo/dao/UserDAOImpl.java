package com.example.demo.dao;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.example.demo.model.User;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class UserDAOImpl extends AbstractHibernateDAO<User> implements UserDAO {

    private static final String HQL_FIND_USER_BY_USERNAME = "FROM User WHERE username = :username";
    private static final String HQL_FIND_USER_BY_USERNAME_AND_PASSWORD = "FROM User WHERE username = :username AND password = :password";
    private static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username {}";
    private static final String USER_NOT_FOUND_WITH_USERNAME_AND_PASSWORD = "User not found with username {} and password {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    public UserDAOImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            TypedQuery<User> query = entityManager.createQuery(HQL_FIND_USER_BY_USERNAME, User.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            LOGGER.warn(USER_NOT_FOUND_WITH_USERNAME, username);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        try {
            TypedQuery<User> query = entityManager.createQuery(HQL_FIND_USER_BY_USERNAME_AND_PASSWORD, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            LOGGER.error(USER_NOT_FOUND_WITH_USERNAME_AND_PASSWORD, username, password);
            return Optional.empty();
        }
    }


    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

}
