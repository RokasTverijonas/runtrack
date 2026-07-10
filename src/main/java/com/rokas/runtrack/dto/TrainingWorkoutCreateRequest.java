package com.rokas.runtrack.dto;

public record TrainingWorkoutCreateRequest(
        Integer weekNumber,
        String dayOfWeek,
        String workoutType,
        Double distanceKm,
        String paceTarget,
        String description
) {
}
