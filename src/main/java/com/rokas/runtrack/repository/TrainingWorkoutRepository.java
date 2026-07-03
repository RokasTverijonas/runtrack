package com.rokas.runtrack.repository;

import com.rokas.runtrack.entity.TrainingPlan;
import com.rokas.runtrack.entity.TrainingWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingWorkoutRepository extends JpaRepository<TrainingWorkout, Long> {

    List<TrainingWorkout> findByTrainingPlan(TrainingPlan trainingPlan);
}
