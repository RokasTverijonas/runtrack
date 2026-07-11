package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.TrainingPlanCreateRequest;
import com.rokas.runtrack.dto.TrainingPlanResponse;
import com.rokas.runtrack.entity.TrainingPlan;
import com.rokas.runtrack.entity.TrainingPlanStatus;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.exception.ResourceNotFoundException;
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
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " was not found"));
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
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " was not found"));

        return trainingPlanRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TrainingPlanResponse getTrainingPlanByUserAndId(Long userId, Long planId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " was not found"));

        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Training plan with id: " + planId + " was not found"));

        if (!trainingPlan.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Training plan does not belong to this user");
        }
        return mapToResponse(trainingPlan);
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
