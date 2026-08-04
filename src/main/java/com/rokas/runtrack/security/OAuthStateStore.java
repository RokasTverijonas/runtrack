package com.rokas.runtrack.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OAuthStateStore {

    private static final long TTL_SECONDS = 600;

    private record StateEntry(
            Long userId,
            Instant expiresAt
    ) {}

    private final Map<String, StateEntry> store = new ConcurrentHashMap<>();

    public String createState(Long userId) {
        String state = UUID.randomUUID().toString();
        store.put(state, new StateEntry(userId, Instant.now().plusSeconds(TTL_SECONDS)));
        return state;
    }

    public Long consumeState(String state) {
        StateEntry entry = store.remove(state);

        if(entry == null || entry.expiresAt().isBefore(Instant.now())) {
            return null;
        }
        return entry.userId;
    }
}
