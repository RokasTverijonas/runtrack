package com.rokas.runtrack.dto;

import java.util.List;

public record TrainingPlanAiResponse(
        String planSummary,
        List<TrainingWorkoutAiResponse> workouts
) {
}
