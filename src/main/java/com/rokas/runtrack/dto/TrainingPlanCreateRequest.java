package com.rokas.runtrack.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TrainingPlanCreateRequest(

        @NotBlank(message = "Race type is required")
        String raceType,

        @NotNull(message = "Race date is required")
        @Future(message = "Race date must be in the future")
        LocalDate raceDate,

        @NotBlank(message = "Plan summary is required")
        String planSummary
) {
}
