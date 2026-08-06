package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.StravaActivityResponse;
import com.rokas.runtrack.dto.StravaAthleteResponse;
import com.rokas.runtrack.dto.StravaTokenResponse;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.security.OAuthStateStore;
import com.rokas.runtrack.service.StravaOAuthService;
import com.rokas.runtrack.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/strava")
public class StravaController {

    private final StravaOAuthService stravaOAuthService;
    private final UserService userService;
    private final OAuthStateStore oAuthStateStore;

    public StravaController(StravaOAuthService stravaOAuthService, UserService userService, OAuthStateStore oAuthStateStore) {
        this.stravaOAuthService = stravaOAuthService;
        this.userService = userService;
        this.oAuthStateStore = oAuthStateStore;
    }

    @GetMapping("/connect")
    public ResponseEntity<Void> connect() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        String state = oAuthStateStore.createState(currentUser.getId());

        String authorizationUrl1 = stravaOAuthService.buildAuthorizationUrl(state);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(authorizationUrl1))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<StravaTokenResponse> callback(@RequestParam("code") String code, @RequestParam("state") String state) {

        Long userId = oAuthStateStore.consumeState(state);
        if(userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        StravaTokenResponse response = stravaOAuthService.exchangeCodeForToken(code);

        stravaOAuthService.saveStravaToken(response, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/athlete")
    public ResponseEntity<StravaAthleteResponse> getCurrentAthlete() {

        StravaAthleteResponse athlete = stravaOAuthService.getCurrentAthlete();

        return ResponseEntity.ok(athlete);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<StravaActivityResponse>> getActivities() {
        final List<StravaActivityResponse> activities = stravaOAuthService.getActivities();

        return ResponseEntity.ok(activities);
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> syncActivities() {
        stravaOAuthService.syncActivities();
        return ResponseEntity.ok().build();
    }



}
