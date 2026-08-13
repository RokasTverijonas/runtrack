package com.rokas.runtrack.dto;

import java.util.List;

public record GeminiResponse(
        List<Candidate> candidates
) {
    public record Candidate(
            GeminiContent content
    ) {}

    public String extractText() {
        return candidates()
                .get(0)
                .content()
                .parts()
                .get(0)
                .text();
    }
}
