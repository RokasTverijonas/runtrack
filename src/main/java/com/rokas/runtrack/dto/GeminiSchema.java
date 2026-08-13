package com.rokas.runtrack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeminiSchema(
        String type,
        Map<String, GeminiSchema> properties,
        GeminiSchema items,
        List<String> required
) {
}
