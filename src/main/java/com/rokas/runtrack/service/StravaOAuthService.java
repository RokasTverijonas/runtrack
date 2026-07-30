package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.StravaTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class StravaOAuthService {

    @Value("${strava.client-id}")
    private String clientId;

    @Value("${strava.client-secret}")
    private String clientSecret;

    @Value("${strava.redirect-uri}")
    private String redirectUri;

    private final RestClient restClient;

    public StravaOAuthService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public String buildAuthorizationUrl() {
        return "https://www.strava.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&approval_prompt=force"
                + "&scope=read,activity:read_all";
    }

    public StravaTokenResponse exchangeCodeForToken(String code) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("grant_type", "authorization_code");

        return restClient
                .post()
                .uri("https://www.strava.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(StravaTokenResponse.class);

    }
}
