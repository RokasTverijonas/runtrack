package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.TrainingPlanCreateRequest;
import com.rokas.runtrack.dto.TrainingPlanResponse;
import com.rokas.runtrack.service.TrainingPlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping
    public TrainingPlanResponse createTrainingPlan(@Valid @RequestBody TrainingPlanCreateRequest request) {
        return trainingPlanService.createTrainingPlan(request);
    }

    @GetMapping
    public List<TrainingPlanResponse> getCurrentUserTrainingPlans() {
        return trainingPlanService.getCurrentUserTrainingPlans();
    }

    @GetMapping("/{planId}")
    public TrainingPlanResponse getTrainingPlan(@PathVariable("planId") Long planId) {
        return trainingPlanService.getTrainingPlan(planId);
    }
}
