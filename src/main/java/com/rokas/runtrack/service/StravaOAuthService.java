package com.rokas.runtrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StravaOAuthService {

    @Value("${strava.client-id}")
    private String clientId;

    @Value("${strava.client-secret}")
    private String clientSecret;

    @Value("${strava.redirect-uri}")
    private String redirectUri;

    public String buildAuthorizationUrl() {
        return "https://www.strava.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&approval_prompt=force"
                + "&scope=read,activity:read_all";
    }
}
