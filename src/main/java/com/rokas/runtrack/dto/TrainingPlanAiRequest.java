package com.rokas.runtrack.dto;

import java.time.LocalDate;
import java.util.List;

public record TrainingPlanAiRequest(
        String raceType,
        Double distanceKm,
        LocalDate raceDate,
        Integer weeksUntilRace,

        Double avgWeeklyKm,
        Double longestRunKm,
        Double avgPaceMinPerKm,

        List<ActivityResponse> recentActivitiesa
) {
}
