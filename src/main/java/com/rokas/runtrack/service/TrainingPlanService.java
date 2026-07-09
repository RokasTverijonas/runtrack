package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.TrainingPlanCreateRequest;
import com.rokas.runtrack.dto.TrainingPlanResponse;
import com.rokas.runtrack.entity.TrainingPlan;
import com.rokas.runtrack.entity.TrainingPlanStatus;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.repository.TrainingPlanRepository;
import com.rokas.runtrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository, UserRepository userRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.userRepository = userRepository;
    }

    public TrainingPlanResponse createTrainingPlan(Long userId, TrainingPlanCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " was not found"));
        TrainingPlan trainingPlan = new TrainingPlan();

        trainingPlan.setUser(user);
        trainingPlan.setRaceType(request.raceType());
        trainingPlan.setRaceDate(request.raceDate());
        trainingPlan.setPlanSummary(request.planSummary());
        trainingPlan.setStatus(TrainingPlanStatus.ACTIVE);

        TrainingPlan savedTrainingPlan = trainingPlanRepository.save(trainingPlan);

        return mapToResponse(savedTrainingPlan);
    }

    public List<TrainingPlanResponse> getTrainingPlansByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " was not found"));

        return trainingPlanRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TrainingPlanResponse mapToResponse(TrainingPlan trainingPlan) {
        return new TrainingPlanResponse(
                trainingPlan.getId(),
                trainingPlan.getUser().getId(),
                trainingPlan.getRaceType(),
                trainingPlan.getRaceDate(),
                trainingPlan.getPlanSummary(),
                trainingPlan.getStatus()
        );
    }
}
