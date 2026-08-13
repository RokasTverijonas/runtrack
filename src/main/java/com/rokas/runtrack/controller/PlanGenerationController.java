package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.TrainingPlanAiRequest;
import com.rokas.runtrack.dto.TrainingPlanAiResponse;
import com.rokas.runtrack.dto.TrainingPlanGenerateRequest;
import com.rokas.runtrack.service.AiService;
import com.rokas.runtrack.service.PlanGenerationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training-plans")
public class PlanGenerationController {

    private final PlanGenerationService planGenerationService;
    private final AiService aiService;

    public PlanGenerationController(PlanGenerationService planGenerationService, AiService aiService) {
        this.planGenerationService = planGenerationService;
        this.aiService = aiService;
    }

    @PostMapping("/generate")
    public TrainingPlanAiResponse generatePlan(@RequestBody TrainingPlanGenerateRequest request) {

        TrainingPlanAiRequest aiRequest = planGenerationService.buildAiRequest(request);

        return  aiService.generateTrainingPlan(aiRequest);
    }
}
