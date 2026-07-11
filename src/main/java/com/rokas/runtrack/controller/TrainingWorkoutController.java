package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.TrainingWorkoutCreateRequest;
import com.rokas.runtrack.dto.TrainingWorkoutResponse;
import com.rokas.runtrack.service.TrainingWorkoutService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainingWorkoutController {
    private final TrainingWorkoutService trainingWorkoutService;

    public TrainingWorkoutController(TrainingWorkoutService trainingWorkoutService) {
        this.trainingWorkoutService = trainingWorkoutService;
    }

    @PostMapping("/training-plans/{planId}/workouts")
    public TrainingWorkoutResponse createWorkout(@PathVariable("planId") Long planId, @Valid @RequestBody TrainingWorkoutCreateRequest request) {
        return trainingWorkoutService.createWorkout(planId, request);

    }

    @GetMapping("/training-plans/{planId}/workouts")
    public List<TrainingWorkoutResponse> getWorkoutsByPlan(@PathVariable("planId") Long planId) {
        return trainingWorkoutService.getWorkoutsByPlan(planId);
    }

    @PatchMapping("/workouts/{workoutId}/complete")
    public TrainingWorkoutResponse markWorkoutAsCompleted(@PathVariable("workoutId") Long workoutId) {
        return trainingWorkoutService.markWorkoutAsCompleted(workoutId);
    }
}
