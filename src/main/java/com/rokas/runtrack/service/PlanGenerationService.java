package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.ActivityResponse;
import com.rokas.runtrack.dto.StatsResponse;
import com.rokas.runtrack.dto.TrainingPlanAiRequest;
import com.rokas.runtrack.dto.TrainingPlanGenerateRequest;
import com.rokas.runtrack.entity.Activity;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class PlanGenerationService {

    private final UserService userService;
    private final StatsService statsService;
    private final ActivityRepository activityRepository;

    public PlanGenerationService(UserService userService, StatsService statsService, ActivityRepository activityRepository) {
        this.userService = userService;
        this.statsService = statsService;
        this.activityRepository = activityRepository;
    }

    public TrainingPlanAiRequest buildAiRequest(TrainingPlanGenerateRequest request) {
        User user = userService.getCurrentAuthenticatedUser();
        List<Activity> activities = activityRepository.findByUser(user);
        StatsResponse stats = statsService.getCurrentUserStats();

        Integer weeksUntilRace = (int) ChronoUnit.WEEKS.between(
                LocalDate.now(),
                request.raceDate()
        );

        List<ActivityResponse> recentActivities = activities.stream()
                .map(activity -> new ActivityResponse(
                        activity.getId(),
                        activity.getUser().getId(),
                        activity.getName(),
                        activity.getDistanceMeters(),
                        activity.getElapsedSeconds(),
                        activity.getAvgPace(),
                        activity.getElevationGain(),
                        activity.getStartedAt(),
                        activity.getActivityType()
                ))
                .toList();

        return new TrainingPlanAiRequest(
                request.raceType(),
                request.distanceKm(),
                request.raceDate(),
                weeksUntilRace,
                stats.avgWeeklyKm(),
                stats.longestRunKm(),
                stats.averagePace(),
                recentActivities
        );

    }
}
