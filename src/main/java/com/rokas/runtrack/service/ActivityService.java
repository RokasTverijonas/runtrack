package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.ActivityCreateRequest;
import com.rokas.runtrack.dto.ActivityResponse;
import com.rokas.runtrack.entity.Activity;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.exception.ResourceNotFoundException;
import com.rokas.runtrack.repository.ActivityRepository;
import com.rokas.runtrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository, UserService userService) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public ActivityResponse createActivity(Long userId, ActivityCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " was not found"));
        Activity activity = new Activity();
        activity.setUser(user);
        activity.setName(request.name());
        activity.setDistanceMeters(request.distanceMeters());
        activity.setElapsedSeconds(request.elapsedSeconds());
        activity.setElevationGain(request.elevationGain());
        activity.setStartedAt(request.startedAt());
        activity.setActivityType(request.activityType());

        activity.setAvgPace(calculateAvgPace(request.distanceMeters(), request.elapsedSeconds()));

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);
    }

    public List<ActivityResponse> getActivitiesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " was not found"));
        return activityRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ActivityResponse mapToResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getUser().getId(),
                activity.getName(),
                activity.getDistanceMeters(),
                activity.getElapsedSeconds(),
                activity.getAvgPace(),
                activity.getElevationGain(),
                activity.getStartedAt(),
                activity.getActivityType()
        );
    }

    private Double calculateAvgPace(Double distanceMeters, Integer elapsedSeconds) {
        Double distanceKm = distanceMeters / 1000.0;
        Double durationMinutes = elapsedSeconds / 60.0;
        Double pace = durationMinutes / distanceKm;

        return pace;
    }

    public List<ActivityResponse> getCurrentUserActivities() {
        User user = userService.getCurrentAuthenticatedUser();

        return activityRepository.findByUserOrderByStartedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

}
