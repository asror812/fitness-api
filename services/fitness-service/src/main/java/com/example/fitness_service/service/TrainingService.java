package com.example.fitness_service.service;



import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Getter
@RequiredArgsConstructor
public class TrainingService {

  /*       private final TraineeRepository traineeRepository;
        private final TrainerRepository trainerRepository;
        private final TrainingDAO dao;
        private final TrainingMapper mapper;
        private final Class<Training> entityClass = Training.class;
        private static final String TRAINER_NOT_FOUND_WITH_USERNAME = "Trainer with id %s not found";
        private static final String TRAINEE_NOT_FOUND_WITH_USERNAME = "Trainee with  %s not found";
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        private final TrainerWorkloadJmsProducer consumer;
        private final TrainingCreationRequestCounterMetric trainingCreationRequestCounterMetric;

        @Transactional
        public void create(TrainingCreateRequestDTO createDTO) {
                Trainee trainee = traineeRepository.findById(createDTO.get)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                TRAINEE_NOT_FOUND_WITH_USERNAME
                                                                .formatted(createDTO.getTraineeUsername())));
                Trainer trainer = trainerDAO.findByUsername(createDTO.getTrainerUsername())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                TRAINER_NOT_FOUND_WITH_USERNAME
                                                                .formatted(createDTO.getTrainerUsername())));

                Training training = new Training();
                training.setTrainee(trainee);
                training.setTrainer(trainer);
                training.setTrainingDate(createDTO.getTrainingDate());
                training.setDuration(createDTO.getDuration());
                training.setTrainingType(trainer.getSpecialization());
                training.setTrainingName(createDTO.getTrainingName());

                trainee.getTrainers().add(trainer);
                trainer.getTrainees().add(trainee);

                traineeDAO.update(trainee);
                trainerDAO.update(trainer);

                dao.create(training);

                TrainerWorkloadRequestDTO trainerWorkloadRequestDTO = TrainerWorkloadRequestDTO.builder()
                                .trainerUsername(trainer.getUser().getUsername())
                                .trainerFirstName(trainer.getUser().getFirstName())
                                .trainerLastName(trainer.getUser().getLastName())
                                .duration(training.getDuration())
                                .trainingDate(LocalDate.parse(dateFormat.format(training.getTrainingDate())))
                                .actionType(ActionType.ADD)
                                .build();

                consumer.updateTrainingSession(trainerWorkloadRequestDTO);

                trainingCreationRequestCounterMetric.incrementTrainingCreationRequestCounter();
        }

        public List<TrainingResponseDTO> getTraineeTrainings(String username, Date from, Date to, String trainerName,
                        String trainingType) {

                return dao.findTraineeTrainings(username, from, to, trainerName, trainingType)
                                .stream()
                                .map(mapper::toResponseDTO)
                                .toList();
        }

        public List<TrainingResponseDTO> getTrainerTrainings(String username, Date from, Date to, String traineeName) {
                return dao.findTrainerTrainings(username, from, to, traineeName)
                                .stream()
                                .map(mapper::toResponseDTO)
                                .toList();
        }

        protected TrainingUpdateResponseDTO internalUpdate(TrainingUpdateRequestDTO requestDTO) {
                throw new UnsupportedOperationException("Unimplemented method 'internalUpdate'");
        } */

}
