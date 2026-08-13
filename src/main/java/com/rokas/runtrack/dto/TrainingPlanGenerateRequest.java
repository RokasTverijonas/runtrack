package com.rokas.runtrack.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TrainingPlanGenerateRequest(

        @NotBlank
        String raceType,

        @NotNull
        Double distanceKm,

        @NotNull
        @Future
        LocalDate raceDate
) {
}
