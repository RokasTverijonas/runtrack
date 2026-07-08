package com.rokas.runtrack.dto;

import java.time.LocalDate;

public record WeeklyStatsResponse(
        LocalDate weekStart,
        Double totalDistanceKm,
        Double averagePace,
        Integer totalRuns
) {

}
