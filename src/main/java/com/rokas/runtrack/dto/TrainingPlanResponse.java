package com.rokas.runtrack.dto;

import com.rokas.runtrack.entity.TrainingPlanStatus;

import java.time.LocalDate;

public record TrainingPlanResponse(
        Long id,
        Long userId,
        String raceType,
        LocalDate raceDate,
        String planSummary,
        TrainingPlanStatus status
) {
}
