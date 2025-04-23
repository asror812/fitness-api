package com.example.demo.service;

import com.example.demo.dao.TraineeDAO;
import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.request.ActionType;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TraineeTrainersUpdateRequestDTO;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.dto.request.TraineeTrainersUpdateRequestDTO.TrainerDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.dto.response.TraineeResponseDTO;
import com.example.demo.dto.response.TraineeUpdateResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.jms.TrainerWorkloadJmsProducer;
import com.example.demo.mapper.TraineeMapper;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.metric.TraineeSignUpRequestCountMetrics;
import com.example.demo.model.Trainee;
import com.example.demo.model.Trainer;
import com.example.demo.model.Training;
import com.example.demo.model.User;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Getter
public class TraineeService
        extends
        AbstractGenericService<Trainee, TraineeSignUpRequestDTO, TraineeUpdateRequestDTO, TraineeResponseDTO, TraineeUpdateResponseDTO> {

    private final TraineeDAO dao;
    private final TrainerDAO trainerDAO;
    private final UserDAO userDAO;
    private final AuthService authService;
    private final TraineeMapper mapper;
    private final TrainerMapper trainerMapper;
    private final Class<Trainee> entityClass = Trainee.class;
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeService.class);
    private final TrainerWorkloadJmsProducer producer;

    private static final String TRAINEE_NOT_FOUND_WITH_USERNAME = "Trainee with username %s not found";
    private static final String TRAINER_NOT_FOUND_WITH_USERNAME = "Trainer with username %s not found";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final TraineeSignUpRequestCountMetrics signUpRequestCountMetrics;

    @Transactional
    public SignUpResponseDTO register(TraineeSignUpRequestDTO requestDTO) {
        SignUpResponseDTO register = authService.register(requestDTO);

        User user = new User(requestDTO.getFirstName(), requestDTO.getLastName(),
                register.getUsername(), register.getPassword(), true);

        Trainee trainee = new Trainee();
        trainee.setAddress(requestDTO.getAddress());
        trainee.setDateOfBirth(requestDTO.getDateOfBirth());
        trainee.setUser(user);

        dao.create(trainee);

        signUpRequestCountMetrics.increment();
        return register;
    }

    @Override
    protected TraineeUpdateResponseDTO internalUpdate(TraineeUpdateRequestDTO updateDTO) {
        String username = updateDTO.getUsername();

        Trainee trainee = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username)));

        mapper.toEntity(updateDTO, trainee);
        dao.update(trainee);

        return mapper.toUpdateResponseDTO(trainee);
    }

    public TraineeResponseDTO findByUsername(String username) {
        Optional<Trainee> existingTrainee = dao.findByUsername(username);

        if (existingTrainee.isEmpty()) {
            throw new EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username));
        }
        return mapper.toResponseDTO(existingTrainee.get());
    }

    @Transactional
    public void delete(String username) {
        Trainee trainee = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username)));

        List<Training> trainings = trainee.getTrainings();

        for (Training training : trainings) {
            training.setTrainee(null);
        }

        dao.delete(trainee);

        for (Training training : trainings) {
            Date trainingDate = training.getTrainingDate();

            if (trainingDate.compareTo(new Date()) > 0) {
                notifyTrainerDeletion(training);
            }
        }

    }

    private void notifyTrainerDeletion(Training training) {
        TrainerWorkloadRequestDTO requestDTO = TrainerWorkloadRequestDTO.builder()
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .trainerFirstName(training.getTrainer().getUser().getFirstName())
                .trainerLastName(training.getTrainer().getUser().getLastName())
                .duration(training.getDuration())
                .trainingDate(LocalDate.parse(dateFormat.format(training.getTrainingDate())))
                .actionType(ActionType.DELETE)
                .build();

        producer.updateTrainingSession(requestDTO);
    }

    @Transactional
    public void setStatus(String username, Boolean status) {
        Trainee trainee = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username)));

        User user = trainee.getUser();

        if (Objects.equals(user.getActive(), status)) {
            LOGGER.warn("'{}' already {}", trainee, status);
            throw new IllegalStateException(String.format("'%s' is already %s", username, status));
        }

        user.setActive(status);
        userDAO.update(user);
    }

    public List<TrainerResponseDTO> getNotAssignedTrainers(String username) {
        Trainee trainee = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username)));

        Set<Trainer> traineeTrainers = Set.copyOf(trainee.getTrainers());

        return trainerDAO.getAll().stream()
                .filter(trainer -> !traineeTrainers.contains(trainer))
                .map(trainerMapper::toResponseDTO)
                .toList();
    }

    public List<TrainerResponseDTO> updateTraineeTrainers(String username,
            TraineeTrainersUpdateRequestDTO requestDTO) {
        Trainee trainee = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        TRAINEE_NOT_FOUND_WITH_USERNAME.formatted(username)));

        Set<Trainer> trainers = new HashSet<>();
        for (TrainerDTO dto : requestDTO.getTrainers()) {
            Trainer trainer = trainerDAO.findByUsername(dto.getUsername()).orElseThrow(
                    () -> new EntityNotFoundException(
                            TRAINER_NOT_FOUND_WITH_USERNAME.formatted(dto.getUsername())));

            trainers.add(trainer);
        }

        trainee.setTrainers(trainers);

        dao.update(trainee);

        return trainers.stream()
                .map(trainerMapper::toResponseDTO)
                .toList();
    }

}
