package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.SignUpRequestDTO;
import com.example.demo.dto.response.SignInResponseDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.TooManyRequestsException;
import com.example.demo.model.User;
import com.example.demo.security.JwtService;
import com.example.demo.utils.BruteForceProtectorService;

import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsernameGeneratorService usernameGeneratorService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BruteForceProtectorService bruteForceProtectorService;

    @Mock
    private JwtService jwtService;

    @Test
    void changePassword_ShouldBe_Ok() {
        User user = new User("asror", "r", "asror.r", "123455678", true);
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("asror.r", "123455678", "098765432");

        when(userDAO.findByUsernameAndPassword("asror.r", "123455678")).thenReturn(Optional.of(user));

        authService.changePassword(requestDTO);
        verify(userDAO, times(1)).update(user);
    }

    @Test
    void changePassword_ShouldReturn_InvalidCredentialsException() {
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("asror.r", "123455678", "098765432");
        assertThrows(InvalidCredentialsException.class, () -> authService.changePassword(requestDTO));
    }

    @Test
    void login_ShouldReturnSignInResponseDTO() {
        SignInRequestDTO requestDTO = new SignInRequestDTO("asror.r", "123456788");
        when(userDAO.findByUsernameAndPassword("asror.r", "123456788")).thenReturn(Optional.of(new User()));
        when(jwtService.generateToken("asror.r")).thenReturn("a");
        authService.login(requestDTO);
        SignInResponseDTO login = authService.login(requestDTO);
        assertEquals("a", login.getToken());
    }

    @Test
    void login_ShouldReturn_InvalidCredentialsException() {
        SignInRequestDTO requestDTO = new SignInRequestDTO("asror.r", "123456788");
        assertThrows(InvalidCredentialsException.class, () -> authService.login(requestDTO));
    }

    @Test
    void register_ShouldReturnSignUpResponseDTO() {
        SignUpRequestDTO requestDTO = new SignUpRequestDTO("asror", "r");
        when(usernameGeneratorService.generateUsername("asror", "r")).thenReturn("asror.r");
        when(jwtService.generateToken("asror.r")).thenReturn("a");

        SignUpResponseDTO register = authService.register(requestDTO);

        assertEquals("a", register.getToken());
        assertEquals("asror.r", register.getUsername());
    }

    @Test
    void login_ShouldThrowTooManyRequestsException() {
        SignInRequestDTO requestDTO = new SignInRequestDTO("asror.r", "123456788");
        when(bruteForceProtectorService.isBlocked("asror.r")).thenReturn(true);

        assertThrows(TooManyRequestsException.class, () -> authService.login(requestDTO));
    }
}
