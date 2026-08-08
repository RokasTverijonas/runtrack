package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.StatsResponse;
import com.rokas.runtrack.dto.WeeklyStatsResponse;
import com.rokas.runtrack.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public StatsResponse getCurrentUserStats() {
        return statsService.getCurrentUserStats();
    }

    @GetMapping("/weekly")
    public List<WeeklyStatsResponse> getCurrentUserWeeklyStats() {
        return statsService.getCurrentUserWeeklyStats();
    }
}
