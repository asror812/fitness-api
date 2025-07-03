package com.example.demo.service;

import com.example.demo.dao.TraineeDAO;
import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.Trainee;
import com.example.demo.model.Trainer;
import com.example.demo.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username %s";

    private static final String TRAINEE_NOT_FOUND_WITH_USERNAME = "Trainee with username %s not found";
    private static final String TRAINER_NOT_FOUND_WITH_USERNAME = "Trainer with username %s not found";

    private final UserDAO userDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final TrainerDAO trainerDAO;
    private final TraineeDAO traineeDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_WITH_USERNAME.formatted(username)));
    }

    @Transactional
    public void setStatus(String username, boolean status, String role) {
        User user;

        switch (role.toLowerCase()) {
            case "trainee" -> {
                Trainee trainee = traineeDAO.findByUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException(
                                TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username)));
                user = trainee.getUser();
            }
            case "trainer" -> {
                Trainer trainer = trainerDAO.findByUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException(
                                TRAINER_NOT_FOUND_WITH_USERNAME.formatted(username)));
                user = trainer.getUser();
            }
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        }

        if (user.isActive() == status) {
            LOGGER.warn("'{}'s status already set to {}", username, status);
            throw new IllegalStateException(String.format("'%s's status already set to %s", username, status));
        }

        user.setActive(status);
        userDAO.update(user);
    }
}
