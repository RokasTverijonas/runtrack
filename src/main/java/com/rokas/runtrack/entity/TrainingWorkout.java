package com.rokas.runtrack.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "training_workouts")
public class TrainingWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_plan_id")
    private TrainingPlan trainingPlan;

    private Integer weekNumber;
    private String dayOfWeek;
    private String workoutType;
    private Double distanceKm;
    private String paceTarget;

    @Column(length = 3000)
    private String description;
    private Boolean completed;

    public TrainingWorkout() {
    }

    public TrainingWorkout(TrainingPlan trainingPlan, Integer weekNumber, String dayOfWeek, String workoutType, Double distanceKm, String paceTarget, String description, Boolean completed) {
        this.trainingPlan = trainingPlan;
        this.weekNumber = weekNumber;
        this.dayOfWeek = dayOfWeek;
        this.workoutType = workoutType;
        this.distanceKm = distanceKm;
        this.paceTarget = paceTarget;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public TrainingPlan getTrainingPlan() {
        return trainingPlan;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public String getPaceTarget() {
        return paceTarget;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setTrainingPlan(TrainingPlan trainingPlan) {
        this.trainingPlan = trainingPlan;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setPaceTarget(String paceTarget) {
        this.paceTarget = paceTarget;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
