package com.rokas.runtrack.dto;

public record GeminiGenerationConfig(
        String responseMimeType,
        GeminiSchema responseSchema,
        Double temperature
) {
}
