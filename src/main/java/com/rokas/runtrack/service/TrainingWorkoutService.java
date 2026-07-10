package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.TrainingWorkoutCreateRequest;
import com.rokas.runtrack.dto.TrainingWorkoutResponse;
import com.rokas.runtrack.entity.TrainingPlan;
import com.rokas.runtrack.entity.TrainingWorkout;
import com.rokas.runtrack.repository.TrainingPlanRepository;
import com.rokas.runtrack.repository.TrainingWorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingWorkoutService {
    private final TrainingWorkoutRepository trainingWorkoutRepository;
    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingWorkoutService(TrainingWorkoutRepository trainingWorkoutRepository, TrainingPlanRepository trainingPlanRepository) {
        this.trainingWorkoutRepository = trainingWorkoutRepository;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    public TrainingWorkoutResponse createWorkout(Long planId, TrainingWorkoutCreateRequest request) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Training plan with id: " + planId + " was not found"));

        TrainingWorkout trainingWorkout = new TrainingWorkout();
        trainingWorkout.setTrainingPlan(trainingPlan);
        trainingWorkout.setWeekNumber(request.weekNumber());
        trainingWorkout.setDayOfWeek(request.dayOfWeek());
        trainingWorkout.setWorkoutType(request.workoutType());
        trainingWorkout.setDistanceKm(request.distanceKm());
        trainingWorkout.setPaceTarget(request.paceTarget());
        trainingWorkout.setDescription(request.description());
        trainingWorkout.setCompleted(false);

        TrainingWorkout savedTrainingWorkout = trainingWorkoutRepository.save(trainingWorkout);

        return mapToResponse(savedTrainingWorkout);

    }

    public List<TrainingWorkoutResponse> getWorkoutsByPlan(Long planId) {
        TrainingPlan trainingPlan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Training plan with id: " + planId + " was not found"));

        return trainingWorkoutRepository.findByTrainingPlan(trainingPlan)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TrainingWorkoutResponse markWorkoutAsCompleted(Long workoutId) {
        TrainingWorkout trainingWorkout = trainingWorkoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Training workout with id: " + workoutId + " was not found"));

        trainingWorkout.setCompleted(true);

        TrainingWorkout savedTrainingWorkout = trainingWorkoutRepository.save(trainingWorkout);

        return mapToResponse(savedTrainingWorkout);
    }

    private TrainingWorkoutResponse mapToResponse(TrainingWorkout trainingWorkout) {
        return new TrainingWorkoutResponse(
                trainingWorkout.getId(),
                trainingWorkout.getTrainingPlan().getId(),
                trainingWorkout.getWeekNumber(),
                trainingWorkout.getDayOfWeek(),
                trainingWorkout.getWorkoutType(),
                trainingWorkout.getDistanceKm(),
                trainingWorkout.getPaceTarget(),
                trainingWorkout.getDescription(),
                trainingWorkout.getCompleted()
        );
    }
}
