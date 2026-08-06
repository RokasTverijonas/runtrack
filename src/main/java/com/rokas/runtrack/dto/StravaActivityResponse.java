package com.rokas.runtrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StravaActivityResponse(
        Long id,
        String name,
        String type,
        Double distance,

        @JsonProperty("moving_time")
        Integer movingTime,

        @JsonProperty("elapsed_time")
        Integer elapsedTime,

        @JsonProperty("total_elevation_gain")
        Double totalElevationGain,

        @JsonProperty("start_date")
        OffsetDateTime startDate
) {
}
