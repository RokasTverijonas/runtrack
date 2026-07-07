package com.rokas.runtrack.dto;

import java.time.LocalDateTime;

public record ActivityResponse(
        Long id,
        Long userId,
        String name,
        Double distanceMeters,
        Integer elapsedSeconds,
        Double avgPace,
        Double elevationGain,
        LocalDateTime startedAt,
        String activityType

) {
}
