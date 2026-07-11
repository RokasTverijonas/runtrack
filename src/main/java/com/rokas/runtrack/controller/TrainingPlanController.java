package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.TrainingPlanCreateRequest;
import com.rokas.runtrack.dto.TrainingPlanResponse;
import com.rokas.runtrack.service.TrainingPlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/training-plans")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping
    public TrainingPlanResponse createTrainingPlan(@PathVariable("userId") Long userId, @Valid @RequestBody TrainingPlanCreateRequest request) {
        return trainingPlanService.createTrainingPlan(userId, request);
    }

    @GetMapping
    public List<TrainingPlanResponse> getTrainingPlansByUser(@PathVariable("userId") Long userId) {
        return trainingPlanService.getTrainingPlansByUser(userId);
    }

    @GetMapping("/{planId}")
    public TrainingPlanResponse getTrainingPlanByUserAndId(@PathVariable("userId") Long userId, @PathVariable("planId") Long planId) {
        return trainingPlanService.getTrainingPlanByUserAndId(userId, planId);
    }
}
