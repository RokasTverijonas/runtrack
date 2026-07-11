package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.ActivityCreateRequest;
import com.rokas.runtrack.dto.ActivityResponse;
import com.rokas.runtrack.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ActivityResponse createActivity(@PathVariable Long userId, @Valid @RequestBody ActivityCreateRequest request) {
        return activityService.createActivity(userId, request);
    }

    @GetMapping
    public List<ActivityResponse> getActivitiesByUser(@PathVariable Long userId) {
        return activityService.getActivitiesByUser(userId);
    }
}
