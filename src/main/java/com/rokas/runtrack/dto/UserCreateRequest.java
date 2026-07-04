package com.rokas.runtrack.dto;

public record UserCreateRequest(
        String name,
        String email
) {
}
