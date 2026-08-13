package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestClient restClient;

    private final JsonMapper jsonMapper;

    public AiService(RestClient.Builder builder, JsonMapper jsonMapper) {
        this.restClient = builder.build();
        this.jsonMapper = jsonMapper;
    }

    public TrainingPlanAiResponse generateTrainingPlan(TrainingPlanAiRequest request) {

        String prompt = buildPrompt(request);

        GeminiRequest geminiRequest = buildGeminiRequest(prompt);

        GeminiResponse response = restClient
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/"
                        + model
                        + ":generateContent")
                .header("x-goog-api-key", apiKey)
                .body(geminiRequest)
                .retrieve()
                .body(GeminiResponse.class);

        try {
            String json = response.extractText();
            return jsonMapper.readValue(json, TrainingPlanAiResponse.class);
        } catch (Exception e) {
            throw new IllegalStateException("Was not able to process gemini response", e);
        }
    }

    private String buildPrompt(TrainingPlanAiRequest request) {
        return """
            You are an expert running coach.

            Create a personalized running training plan.

            RUNNER GOAL:
            Race type: %s
            Race distance: %.1f km
            Race date: %s
            Weeks until race: %d

            CURRENT FITNESS:
            Average weekly distance: %.1f km
            Longest run: %.1f km
            Average pace: %.2f min/km

            RECENT ACTIVITIES:
            %s

            TRAINING PLAN REQUIREMENTS:

            1. Create a realistic progression toward the race.
            2. Do not increase training load too aggressively.
            3. Include rest days.
            4. Include easy runs, long runs and quality sessions where appropriate.
            5. For interval workouts specify:
               - number of repetitions
               - distance of each repetition
               - recovery between repetitions
               - target pace
            6. For tempo workouts specify:
               - tempo duration or distance
               - target pace
            7. For long runs specify:
               - distance
               - recommended pace
            8. Every workout must have a detailed description.
            9. The plan must fit the number of weeks available before the race.

            """
                .formatted(
                        request.raceType(),
                        request.distanceKm(),
                        request.raceDate(),
                        request.weeksUntilRace(),
                        request.avgWeeklyKm(),
                        request.longestRunKm(),
                        request.avgPaceMinPerKm(),
                        request.recentActivities()
                );
    }

    public GeminiRequest buildGeminiRequest(String prompt) {

        GeminiPart part = new GeminiPart(prompt);

        GeminiContent content = new GeminiContent(
                "user",
                List.of(part)
        );

        GeminiGenerationConfig generationConfig = new GeminiGenerationConfig(
                "application/json",
                buildResponseSchema(),
                0.7
        );

        return new GeminiRequest(
                List.of(content),
                generationConfig
        );

    }

    private GeminiSchema buildResponseSchema() {
        GeminiSchema workout = new GeminiSchema(
                "OBJECT",
                Map.of(
                        "weekNumber", primitive("INTEGER"),
                        "dayOfWeek", primitive("STRING"),
                        "workoutType", primitive("STRING"),
                        "distanceKm", primitive("NUMBER"),
                        "paceTarget", primitive("STRING"),
                        "description", primitive("STRING")
                ),
                null,
                List.of("weekNumber", "dayOfWeek", "workoutType", "description")
        );

        return new GeminiSchema(
                "OBJECT",
                Map.of(
                        "planSummary", primitive("STRING"),
                        "workouts", new GeminiSchema("ARRAY", null, workout, null)
                ),
                null,
                List.of("planSummary", "workouts")
        );
    }

    private GeminiSchema primitive(String type) {
        return new GeminiSchema(type, null, null, null);
    }


}
