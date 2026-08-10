package com.rokas.runtrack.dto;

public record StatsResponse(
        Long userId,
        Integer totalRuns,
        Double totalDistanceKm,
        Double averagePace,
        Double totalElevationGain,
        Double longestRunKm,
        Double avgWeeklyKm
) {
}
