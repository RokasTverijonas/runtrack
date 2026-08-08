package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.StatsResponse;
import com.rokas.runtrack.dto.WeeklyStatsResponse;
import com.rokas.runtrack.entity.Activity;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.exception.ResourceNotFoundException;
import com.rokas.runtrack.repository.ActivityRepository;
import com.rokas.runtrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    private final UserService userService;
    private final ActivityRepository activityRepository;

    public StatsService(UserService userService, ActivityRepository activityRepository) {
        this.userService = userService;
        this.activityRepository = activityRepository;
    }

    public StatsResponse getCurrentUserStats() {
        User user = userService.getCurrentAuthenticatedUser();
        List<Activity> activities = activityRepository.findByUser(user);

        Integer totalRuns = activities.size();
        Double totalDistanceKm = calculateTotalDistanceKm(activities);
        Double averagePace = calculateAvgPace(activities);
        Double totalElevationGain = calculateTotalElevationGain(activities);
        Double longestRunKm = calculateLongestRunKm(activities);

        return new StatsResponse(
                user.getId(),
                totalRuns,
                totalDistanceKm,
                averagePace,
                totalElevationGain,
                longestRunKm
        );
    }

    public List<WeeklyStatsResponse> getCurrentUserWeeklyStats() {
        User user = userService.getCurrentAuthenticatedUser();
        List<Activity> activities = activityRepository.findByUser(user);

        Map<LocalDate, List<Activity>> activitiesByWeek = new HashMap<>();

        for(Activity activity : activities) {
            LocalDate activityDate = activity.getStartedAt().toLocalDate();

            LocalDate weekStart = activityDate.with(
                    TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            );

            activitiesByWeek
                    .computeIfAbsent(weekStart, key -> new ArrayList<>())
                    .add(activity);
        }

        List<WeeklyStatsResponse> weeklyStats = new ArrayList<>();

        for(LocalDate weekStart : activitiesByWeek.keySet()) {
            List<Activity> weekActivities = activitiesByWeek.get(weekStart);

            Integer totalRuns = weekActivities.size();

            Double totalDistanceMeters = 0.0;
            Double totalElapsedSeconds = 0.0;

            for(Activity activity : weekActivities) {
                totalDistanceMeters += activity.getDistanceMeters();
                totalElapsedSeconds += activity.getElapsedSeconds();
            }

            Double totalDistanceKm = totalDistanceMeters / 1000.0;

            Double averagePace = 0.0;

            if(totalDistanceKm > 0) {
                Double totalDurationMinutes = totalElapsedSeconds / 60.0;
                averagePace = totalDurationMinutes / totalDistanceKm;
            }

            WeeklyStatsResponse response = new WeeklyStatsResponse(
                    weekStart,
                    totalDistanceKm,
                    averagePace,
                    totalRuns
            );

            weeklyStats.add(response);
        }
        weeklyStats.sort((a, b) -> a.weekStart().compareTo(b.weekStart()));
        return weeklyStats;
    }

    private Double calculateTotalDistanceKm(List<Activity> activities) {
        Double totalMeters = 0.0;
        for(Activity activity : activities) {
            totalMeters += activity.getDistanceMeters();
        }

        return totalMeters / 1000.0;
    }

    private Double calculateAvgPace(List<Activity> activities) {
        if(activities.isEmpty()) {
            return 0.0;
        }
        Double totalDistanceMeters = 0.0;
        Double totalElapsedSeconds = 0.0;

        for(Activity activity : activities) {
            totalDistanceMeters += activity.getDistanceMeters();
            totalElapsedSeconds += activity.getElapsedSeconds();
        }

        if(totalDistanceMeters == 0.0) {
            return 0.0;
        }

        Double totalDistanceKm = totalDistanceMeters / 1000.0;
        Double totalDurationMinutes = totalElapsedSeconds / 60.0;

        return totalDurationMinutes / totalDistanceKm;
    }

    private Double calculateTotalElevationGain(List<Activity> activities) {
        Double totalElevation = 0.0;
        for(Activity activity : activities) {
            totalElevation += activity.getElevationGain();
        }

        return totalElevation;
    }

    private Double calculateLongestRunKm(List<Activity> activities) {
        Double longestRunMeters = 0.0;
        for(Activity activity : activities) {
            if(activity.getDistanceMeters() > longestRunMeters) {
                longestRunMeters = activity.getDistanceMeters();
            }
        }

        return longestRunMeters / 1000.0;
    }
}
