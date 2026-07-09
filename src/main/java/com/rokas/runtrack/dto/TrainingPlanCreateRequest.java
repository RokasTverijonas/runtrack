package com.rokas.runtrack.dto;

import java.time.LocalDate;

public record TrainingPlanCreateRequest(
        String raceType,
        LocalDate raceDate,
        String planSummary
) {
}
