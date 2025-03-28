package com.example.demo.service;

import com.example.demo.dao.UserDAO;
import com.example.demo.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username %s";
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_WITH_USERNAME.formatted(username)));
    }
}
