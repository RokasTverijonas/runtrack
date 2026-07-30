package com.rokas.runtrack.controller;

import com.rokas.runtrack.dto.StravaTokenResponse;
import com.rokas.runtrack.service.StravaOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/strava")
public class StravaController {

    private final StravaOAuthService stravaOAuthService;

    public StravaController(StravaOAuthService stravaOAuthService) {
        this.stravaOAuthService = stravaOAuthService;
    }

    @GetMapping("/connect")
    public ResponseEntity<Void> connect() {
        String authorizationUrl1 = stravaOAuthService.buildAuthorizationUrl();

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(authorizationUrl1))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<StravaTokenResponse> callback(@RequestParam("code") String code) {

        StravaTokenResponse response = stravaOAuthService.exchangeCodeForToken(code);

        return ResponseEntity.ok(response);
    }



}
