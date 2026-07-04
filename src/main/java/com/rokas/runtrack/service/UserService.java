package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.UserCreateRequest;
import com.rokas.runtrack.dto.UserResponse;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserCreateRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());

        User savedUser = userRepository.save(user);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }
}
