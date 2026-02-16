package com.example.auth_service.service;

import com.example.auth_service.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameGeneratorService {

    private final UserRepository userRepository;

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;

        String username = baseUsername;

        int serialNumber = 1;

        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }

        return username;
    }

}
