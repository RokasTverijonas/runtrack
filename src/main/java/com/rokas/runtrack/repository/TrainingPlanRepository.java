package com.rokas.runtrack.repository;

import com.rokas.runtrack.entity.TrainingPlan;
import com.rokas.runtrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {

    List<TrainingPlan> findByUser(User user);
}
