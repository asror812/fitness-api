package com.example.auth_service.service;

import com.example.auth_service.dao.UserRepository;
import com.example.auth_service.dto.request.ChangePasswordRequestDTO;
import com.example.auth_service.dto.request.SignInRequestDTO;
import com.example.auth_service.dto.request.SignUpRequestDTO;
import com.example.auth_service.dto.request.TraineeSignUpRequestDTO;
import com.example.auth_service.dto.request.TrainerSignUpRequestDTO;
import com.example.auth_service.dto.response.SignInResponseDTO;
import com.example.auth_service.dto.response.SignUpResponseDTO;
import com.example.auth_service.exception.InvalidCredentialsException;
import com.example.auth_service.exception.JsonSerializationException;
import com.example.auth_service.exception.TooManyRequestsException;
import com.example.auth_service.jms.dto.TraineeCreateReqDto;
import com.example.auth_service.jms.outbox.EventType;
import com.example.auth_service.jms.outbox.OutboxEvent;
import com.example.auth_service.jms.outbox.OutboxEventRepository;
import com.example.auth_service.jms.outbox.Status;
import com.example.auth_service.model.Role;
import com.example.auth_service.model.User;
import com.example.auth_service.security.JwtService;
import com.example.auth_service.utils.BruteForceProtectorService;
import com.example.auth_service.utils.PasswordGeneratorUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String INCORRECT_USERNAME_AND_PASSWORD = "Username or password is incorrect.: %s - %s";
    private static final String TOO_MANY_REQUESTS = "You have exceeded the maximum number of login attempts. Please try again after some time";

    private final UserRepository userRepository;
    private final OutboxEventRepository outboxEventRepository;

    private final BruteForceProtectorService bruteForceProtectorService;
    private final UsernameGeneratorService usernameGeneratorService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public User register(SignUpRequestDTO requestDTO, Role role) {
        String username = usernameGeneratorService
                .generateUsername(requestDTO.getFirstName(), requestDTO.getLastName());

        String password = PasswordGeneratorUtil.generate();

        User user = User.builder()
                .username(username)
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .password(password)
                .roles(Set.of(role))
                .deleted(false)
                .active(false)
                .build();

        user = userRepository.save(user);

        return user;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        String oldPassword = requestDTO.getOldPassword();

        User user = userRepository.findByUsernameAndPassword(username, oldPassword)
                .orElseThrow(() -> new InvalidCredentialsException(
                        INCORRECT_USERNAME_AND_PASSWORD.formatted(username, oldPassword)));

        user.setPassword(requestDTO.getNewPassword());
        userRepository.save(user);
    }

    @Override
    public SignInResponseDTO login(SignInRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        String oldPassword = requestDTO.getPassword();

        if (bruteForceProtectorService.isBlocked(username)) {
            throw new TooManyRequestsException(TOO_MANY_REQUESTS);
        }

        Optional<User> existingUser = userRepository.findByUsernameAndPassword(username, oldPassword);

        if (existingUser.isEmpty()) {
            bruteForceProtectorService.addFailedAttempt(username);
            throw new InvalidCredentialsException(
                    INCORRECT_USERNAME_AND_PASSWORD.formatted(username, oldPassword));
        }

        User user = existingUser.get();

        bruteForceProtectorService.resetAttempts(username);
        String token = jwtService.generateToken(user);

        return new SignInResponseDTO(token);
    }

    @Override
    public SignUpResponseDTO registerTrainee(TraineeSignUpRequestDTO requestDTO) {

        User user = register(requestDTO, Role.ROLE_TRAINEE);

        TraineeCreateReqDto reqDto = new TraineeCreateReqDto(requestDTO.getDateOfBirth(), requestDTO.getAddress(),
                user.getId());

        String payloadJson;

        try {
            payloadJson = objectMapper.writeValueAsString(reqDto);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Failed to serialize TraineeCreateReqDto to JSON", reqDto, e);
        }

        OutboxEvent event = OutboxEvent.builder()
                .aggregateId(user.getId())
                .aggregateType("User")
                .eventType(EventType.TRAINEE_CREATE_REQUESTED)
                .payload(payloadJson)
                .status(Status.NEW)
                .attempts(0)
                .createdAt(OffsetDateTime.now())
                .nextAttemptAt(OffsetDateTime.now())
                .build();

        outboxEventRepository.save(event);

        return new SignUpResponseDTO(user.getUsername(), user.getPassword());
    }

    @Override
    public SignUpResponseDTO registerTrainer(TrainerSignUpRequestDTO requestDTO) {

        throw new UnsupportedOperationException("Unimplemented method 'registerTrainer'");
    }

}
