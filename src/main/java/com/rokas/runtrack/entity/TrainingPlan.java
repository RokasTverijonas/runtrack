package com.rokas.runtrack.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "training_plans")
public class TrainingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String raceType;
    private LocalDate raceDate;

    @Column(length = 3000)
    private String planSummary;

    @Enumerated(EnumType.STRING)
    private TrainingPlanStatus status;

    public TrainingPlan() {
    }

    public TrainingPlan(User user, String raceType, LocalDate raceDate, String planSummary, TrainingPlanStatus status) {
        this.user = user;
        this.raceType = raceType;
        this.raceDate = raceDate;
        this.planSummary = planSummary;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getRaceType() {
        return raceType;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public String getPlanSummary() {
        return planSummary;
    }

    public TrainingPlanStatus getStatus() {
        return status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRaceType(String raceType) {
        this.raceType = raceType;
    }

    public void setRaceDate(LocalDate raceDate) {
        this.raceDate = raceDate;
    }

    public void setPlanSummary(String planSummary) {
        this.planSummary = planSummary;
    }

    public void setStatus(TrainingPlanStatus status) {
        this.status = status;
    }
}
