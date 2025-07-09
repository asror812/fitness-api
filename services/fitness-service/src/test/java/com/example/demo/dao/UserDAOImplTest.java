package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class UserDAOImplTest {

    @Mock
    private TypedQuery<User> typedQuery;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserDAOImpl userDAO;

    @Test
    void findByUsername_Success() {
        String username = "qwerty";
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(new User());

        Optional<User> byUsername = userDAO.findByUsername(username);
        assertTrue(byUsername.isPresent());
    }

    @Test
    void findByUsername_NoResult() {
        String username = "qwerty";
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        assertEquals(Optional.empty(), userDAO.findByUsername(username));
    }

    @Test
    void findByUsernameAndPassword_Success() {
        String username = "qwerty";
        String password = "1234567890";

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.setParameter("password", password)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(new User());

        Optional<User> byUsername = userDAO.findByUsernameAndPassword(username, password);

        assertTrue(byUsername.isPresent());
    }

    @Test
    void findByUsernameAndPassword_NoResut() {
        String username = "qwerty";
        String password = "1234567890";

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.setParameter("password", password)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        assertEquals(Optional.empty(), userDAO.findByUsernameAndPassword(username, password));
    }


}
