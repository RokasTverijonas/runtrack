package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.TrainingPlanAiRequest;
import com.rokas.runtrack.dto.TrainingPlanAiResponse;
import com.rokas.runtrack.dto.TrainingPlanGenerateRequest;
import com.rokas.runtrack.dto.TrainingPlanResponse;
import com.rokas.runtrack.service.AiService;
import com.rokas.runtrack.service.PlanGenerationService;
import com.rokas.runtrack.service.TrainingPlanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training-plans")
public class PlanGenerationController {

    private final PlanGenerationService planGenerationService;
    private final AiService aiService;
    private final TrainingPlanService trainingPlanService;

    public PlanGenerationController(PlanGenerationService planGenerationService, AiService aiService, TrainingPlanService trainingPlanService) {
        this.planGenerationService = planGenerationService;
        this.aiService = aiService;
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping("/generate")
    public TrainingPlanResponse generatePlan(@RequestBody TrainingPlanGenerateRequest request) {

        TrainingPlanAiRequest aiRequest = planGenerationService.buildAiRequest(request);

        TrainingPlanAiResponse aiResponse = aiService.generateTrainingPlan(aiRequest);

        return trainingPlanService.saveGeneratedPlan(request, aiResponse);
    }
}