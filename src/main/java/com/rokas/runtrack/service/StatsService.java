package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.StatsResponse;
import com.rokas.runtrack.entity.Activity;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.repository.ActivityRepository;
import com.rokas.runtrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public StatsService(UserRepository userRepository, ActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
    }

    public StatsResponse getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " was not found"));
        List<Activity> activities = activityRepository.findByUser(user);

        Integer totalRuns = activities.size();
        Double totalDistanceKm = calculateTotalDistanceKm(activities);
        Double averagePace = calculateAvgPace(activities);
        Double totalElevationGain = calculateTotalElevationGain(activities);
        Double longestRunKm = calculateLongestRunKm(activities);

        return new StatsResponse(
                userId,
                totalRuns,
                totalDistanceKm,
                averagePace,
                totalElevationGain,
                longestRunKm
        );
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
