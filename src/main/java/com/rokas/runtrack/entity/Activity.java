package com.rokas.runtrack.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long stravaActivityId;
    private String name;
    private Double distanceMeters;
    private Integer elapsedSeconds;
    private Double avgPace;
    private Double elevationGain;
    private LocalDateTime startedAt;
    private String activityType;

    public Activity() {
    }

    public Activity(User user, Long stravaActivityId, String name, Double distanceMeters, Integer elapsedSeconds, Double avgPace, Double elevationGain, LocalDateTime startedAt, String activityType) {
        this.user = user;
        this.stravaActivityId = stravaActivityId;
        this.name = name;
        this.distanceMeters = distanceMeters;
        this.elapsedSeconds = elapsedSeconds;
        this.avgPace = avgPace;
        this.elevationGain = elevationGain;
        this.startedAt = startedAt;
        this.activityType = activityType;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getStravaActivityId() {
        return stravaActivityId;
    }

    public String getName() {
        return name;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }

    public Integer getElapsedSeconds() {
        return elapsedSeconds;
    }

    public Double getAvgPace() {
        return avgPace;
    }

    public Double getElevationGain() {
        return elevationGain;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStravaActivityId(Long stravaActivityId) {
        this.stravaActivityId = stravaActivityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public void setElapsedSeconds(Integer elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public void setAvgPace(Double avgPace) {
        this.avgPace = avgPace;
    }

    public void setElevationGain(Double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
