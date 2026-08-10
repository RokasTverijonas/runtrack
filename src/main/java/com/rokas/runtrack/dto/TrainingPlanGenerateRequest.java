package com.rokas.runtrack.dto;

import java.time.LocalDate;

public record TrainingPlanGenerateRequest(
        String raceType,
        Double distanceKm,
        LocalDate raceDate
) {
}
