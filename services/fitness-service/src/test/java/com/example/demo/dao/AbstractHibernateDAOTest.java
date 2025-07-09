package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.exception.DataAccessException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;


class AbstractHibernateDAOTest {

    @Mock
    private EntityManager entityManager;

    private AbstractHibernateDAO<TestEntity> abstractHibernateDAO;

    private static class TestEntity {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        abstractHibernateDAO = new AbstractHibernateDAO<TestEntity>(TestEntity.class) {};
        abstractHibernateDAO.entityManager = entityManager;
    }

    @Test
    void testFindById_Success() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity();
        when(entityManager.find(TestEntity.class, id)).thenReturn(entity);

        Optional<TestEntity> result = abstractHibernateDAO.findById(id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
        verify(entityManager, times(1)).find(TestEntity.class, id);
    }

    @Test
    void testFindById_ThrowsDataAccessException() {
        UUID id = UUID.randomUUID();
        when(entityManager.find(TestEntity.class, id)).thenThrow(new PersistenceException());

        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> abstractHibernateDAO.findById(id));

        assertTrue(exception.getMessage().contains("Failed to get by id"));
        verify(entityManager, times(1)).find(TestEntity.class, id);
    }

    @Test
    void testCreate_Success() {
        TestEntity entity = new TestEntity();

        TestEntity result = abstractHibernateDAO.create(entity);

        assertEquals(entity, result);
        verify(entityManager, times(1)).persist(entity);
    }

    @Test
    void testCreate_ThrowsDataAccessException() {
        TestEntity entity = new TestEntity();
        doThrow(new PersistenceException()).when(entityManager).persist(entity);

        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> abstractHibernateDAO.create(entity));

        assertTrue(exception.getMessage().contains("Failed to create"));
        verify(entityManager, times(1)).persist(entity);
    }

    @Test
    void testUpdate_Success() {
        TestEntity entity = new TestEntity();

        assertDoesNotThrow(() -> abstractHibernateDAO.update(entity));
        verify(entityManager, times(1)).merge(entity);
    }

    @Test
    void testUpdate_ThrowsDataAccessException() {
        TestEntity entity = new TestEntity();
        doThrow(new PersistenceException()).when(entityManager).merge(entity);

        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> abstractHibernateDAO.update(entity));

        assertTrue(exception.getMessage().contains("Failed to update"));
        verify(entityManager, times(1)).merge(entity);
    }
}