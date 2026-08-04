package com.rokas.runtrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StravaActivityResponse(
        Long id,
        String name,
        String type,
        Double distance,

        @JsonProperty("moving_time")
        Integer movingTime,

        @JsonProperty("elapsed_time")
        Integer elapsedTime,

        @JsonProperty("start_date")
        String startDate
) {
}
