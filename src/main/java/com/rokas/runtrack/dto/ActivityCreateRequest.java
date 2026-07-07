package com.rokas.runtrack.dto;

import java.time.LocalDateTime;

public record ActivityCreateRequest(
        String name,
        Double distanceMeters,
        Integer elapsedSeconds,
        Double elevationGain,
        LocalDateTime startedAt,
        String activityType
) {
}
