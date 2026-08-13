package com.rokas.runtrack.dto;

public record TrainingWorkoutAiResponse(
        Integer weekNumber,
        String dayOfWeek,
        String workoutType,
        Double distanceKm,
        String paceTarget,
        String description

) {
}
