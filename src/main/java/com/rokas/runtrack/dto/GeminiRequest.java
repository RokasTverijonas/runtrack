package com.rokas.runtrack.dto;

import java.util.List;

public record GeminiRequest(
        List<GeminiContent> contents,
        GeminiGenerationConfig generationConfig
) {
}
