package com.rokas.runtrack.dto;

public record TrainingWorkoutResponse(
        Long id,
        Long trainingPlanId,
        Integer weekNumber,
        String dayOfWeek,
        String workoutType,
        Double distanceKm,
        String paceTarget,
        String description,
        Boolean completed
) {
}
