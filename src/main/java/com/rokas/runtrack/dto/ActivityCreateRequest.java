package com.rokas.runtrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ActivityCreateRequest(

        @NotBlank(message = "Activity name is required")
        String name,

        @NotNull(message = "Distance is required")
        @Positive(message = "Distance must be positive")
        Double distanceMeters,

        @NotNull(message = "Elapsed time is required")
        @Positive(message = "Elapsed time must be positive")
        Integer elapsedSeconds,

        @NotNull(message = "Elevation gain is required")
        Double elevationGain,

        @NotNull(message = "Start time is required")
        LocalDateTime startedAt,

        @NotBlank(message = "Activity type is required")
        String activityType
) {
}
