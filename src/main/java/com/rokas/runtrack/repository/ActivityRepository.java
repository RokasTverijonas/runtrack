package com.rokas.runtrack.repository;

import com.rokas.runtrack.entity.Activity;
import com.rokas.runtrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByUser(User user);

    List<Activity> findByUserOrderByStartedAtDesc(User user);

    Optional<Activity> findByStravaActivityId(Long stravaActivityId);
}
