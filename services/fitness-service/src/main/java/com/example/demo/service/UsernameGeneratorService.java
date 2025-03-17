package com.example.demo.service;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDAO;

@Service
public class UsernameGeneratorService {

    private final UserDAO userDAO;

    public UsernameGeneratorService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;

        String username = baseUsername;

        int serialNumber = 1;

        while (userDAO.findByUsername(username).isPresent()) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }

        return username;
    }

}
