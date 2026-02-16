/* package com.example.fitness_service.service;

import org.springframework.stereotype.Service;
import com.example.fitness_service.security.JwtService;
import com.example.fitness_service.utils.BruteForceProtectorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private static final String INCORRECT_USERNAME_AND_PASSWORD = "Username or password is incorrect.: %s - %s";
    private static final String TOO_MANY_REQUESTS = "You have exceeded the maximum number of login attempts. Please try again after some time";
    private final BruteForceProtectorService bruteForceProtectorService;

}
 */