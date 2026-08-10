package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.TrainingPlanAiRequest;
import com.rokas.runtrack.dto.TrainingPlanGenerateRequest;
import com.rokas.runtrack.service.PlanGenerationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training-plans")
public class PlanGenerationController {

    private final PlanGenerationService planGenerationService;

    public PlanGenerationController(PlanGenerationService planGenerationService) {
        this.planGenerationService = planGenerationService;
    }

    @PostMapping("/generate")
    public TrainingPlanAiRequest generatePlan(@RequestBody TrainingPlanGenerateRequest request) {
        return  planGenerationService.buildAiRequest(request);
    }
}
