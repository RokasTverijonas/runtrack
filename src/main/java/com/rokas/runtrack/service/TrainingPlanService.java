package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.*;
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
    private final UserService userService;
    private final TrainingWorkoutService trainingWorkoutService;

    public TrainingPlanService(TrainingPlanRepository trainingPlanRepository, UserService userService, TrainingWorkoutService trainingWorkoutService) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.userService = userService;
        this.trainingWorkoutService = trainingWorkoutService;
    }

    public TrainingPlanResponse createTrainingPlan(TrainingPlanCreateRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        TrainingPlan trainingPlan = new TrainingPlan();

        trainingPlan.setUser(user);
        trainingPlan.setRaceType(request.raceType());
        trainingPlan.setRaceDate(request.raceDate());
        trainingPlan.setPlanSummary(request.planSummary());
        trainingPlan.setStatus(TrainingPlanStatus.ACTIVE);

        TrainingPlan savedTrainingPlan = trainingPlanRepository.save(trainingPlan);

        return mapToResponse(savedTrainingPlan);
    }

    public TrainingPlanResponse saveGeneratedPlan(TrainingPlanGenerateRequest originalRequest, TrainingPlanAiResponse aiResponse) {

        User user = userService.getCurrentAuthenticatedUser();

        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setUser(user);
        trainingPlan.setRaceType(originalRequest.raceType());
        trainingPlan.setRaceDate(originalRequest.raceDate());
        trainingPlan.setPlanSummary(aiResponse.planSummary());
        trainingPlan.setStatus(TrainingPlanStatus.ACTIVE);

        TrainingPlan savedPlan = trainingPlanRepository.save(trainingPlan);

        aiResponse.workouts().forEach(workoutAi -> {
            TrainingWorkoutCreateRequest createRequest = new TrainingWorkoutCreateRequest(
                    workoutAi.weekNumber(),
                    workoutAi.dayOfWeek(),
                    workoutAi.workoutType(),
                    workoutAi.distanceKm(),
                    workoutAi.paceTarget(),
                    workoutAi.description()
            );

            trainingWorkoutService.createWorkout(savedPlan.getId(), createRequest);
        });

        return mapToResponse(savedPlan);
    }

    public List<TrainingPlanResponse> getCurrentUserTrainingPlans() {
        User user = userService.getCurrentAuthenticatedUser();

        return trainingPlanRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TrainingPlanResponse getTrainingPlan(Long planId) {
        User user = userService.getCurrentAuthenticatedUser();

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
