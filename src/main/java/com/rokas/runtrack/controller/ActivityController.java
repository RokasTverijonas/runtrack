package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.ActivityResponse;
import com.rokas.runtrack.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getCurrentUserActivities() {
        List<ActivityResponse> activities = activityService.getCurrentUserActivities();

        return ResponseEntity.ok(activities);
    }


}
