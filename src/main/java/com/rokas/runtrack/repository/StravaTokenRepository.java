package com.rokas.runtrack.repository;

import com.rokas.runtrack.entity.StravaToken;
import com.rokas.runtrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StravaTokenRepository extends JpaRepository<StravaToken, Long> {

    Optional<StravaToken> findByUser(User user);
}
