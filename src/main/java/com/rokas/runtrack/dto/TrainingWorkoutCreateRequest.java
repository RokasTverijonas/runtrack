package com.rokas.runtrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TrainingWorkoutCreateRequest(

        @NotNull(message = "Week number is required")
        @Positive(message = "Week number must be positive")
        Integer weekNumber,

        @NotBlank(message = "Day of week is required")
        String dayOfWeek,

        @NotBlank(message = "Workout type is required")
        String workoutType,

        @NotNull(message = "Distance is required")
        @Positive(message = "Distance must be positive")
        Double distanceKm,

        @NotBlank(message = "Pace target is required")
        String paceTarget,

        @NotBlank(message = "Description is required")
        String description
) {
}
