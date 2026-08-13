package com.rokas.runtrack.dto;

import java.util.List;

public record GeminiContent(
        String role,
        List<GeminiPart> parts
) {
}
