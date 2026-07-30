package com.rokas.runtrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StravaTokenResponse(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("expires_at")
        Long expiresAt,

        StravaAthleteResponse athlete
) {
}