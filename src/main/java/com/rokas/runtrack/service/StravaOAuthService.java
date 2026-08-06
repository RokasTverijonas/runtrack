package com.rokas.runtrack.service;

import com.rokas.runtrack.dto.ActivityResponse;
import com.rokas.runtrack.dto.StravaActivityResponse;
import com.rokas.runtrack.dto.StravaAthleteResponse;
import com.rokas.runtrack.dto.StravaTokenResponse;
import com.rokas.runtrack.entity.Activity;
import com.rokas.runtrack.entity.StravaToken;
import com.rokas.runtrack.entity.User;
import com.rokas.runtrack.exception.ResourceNotFoundException;
import com.rokas.runtrack.repository.ActivityRepository;
import com.rokas.runtrack.repository.StravaTokenRepository;
import com.rokas.runtrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class StravaOAuthService {

    @Value("${strava.client-id}")
    private String clientId;

    @Value("${strava.client-secret}")
    private String clientSecret;

    @Value("${strava.redirect-uri}")
    private String redirectUri;

    private final RestClient restClient;
    private final UserService userService;
    private final StravaTokenRepository stravaTokenRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public StravaOAuthService(RestClient.Builder builder, UserService userService, StravaTokenRepository stravaTokenRepository, UserRepository userRepository, ActivityRepository activityRepository) {
        this.restClient = builder.build();
        this.userService = userService;
        this.stravaTokenRepository = stravaTokenRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
    }

    public String buildAuthorizationUrl(String state) {
        return "https://www.strava.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&approval_prompt=force"
                + "&scope=read,activity:read_all"
                + "&state=" + state;
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

    public void saveStravaToken(StravaTokenResponse response, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " was not found"));
        StravaToken token = stravaTokenRepository
                .findByUser(user)
                        .orElse(new StravaToken());

        token.setUser(user);
        token.setAccessToken(response.accessToken());
        token.setRefreshToken(response.refreshToken());
        token.setExpiresAt(
                LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(response.expiresAt()),
                        ZoneId.systemDefault()
                )
        );

        stravaTokenRepository.save(token);

    }

    public StravaToken getCurrentUserToken() {
        User user = userService.getCurrentAuthenticatedUser();

        return stravaTokenRepository
                .findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Strava token for user " + user.getId() + " was not found"));
    }

    public StravaAthleteResponse getCurrentAthlete() {
        StravaToken token = getCurrentUserToken();
        String accessToken = token.getAccessToken();

        return restClient
                .get()
                .uri("https://www.strava.com/api/v3/athlete")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(StravaAthleteResponse.class);
    }

    public List<StravaActivityResponse> getActivities() {
        StravaToken token = getCurrentUserToken();
        String accessToken = token.getAccessToken();

        StravaActivityResponse[] activities = restClient
                .get()
                .uri("https://www.strava.com/api/v3/athlete/activities")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(StravaActivityResponse[].class);

        if(activities == null) {
            return List.of();
        }

        return List.of(activities);
    }

    public void syncActivities() {

        List<StravaActivityResponse> activities = getActivities();
        User user = userService.getCurrentAuthenticatedUser();

        for(StravaActivityResponse activity : activities) {
            if(activityRepository.findByStravaActivityId(activity.id()).isEmpty()) {
                Activity newActivity = new Activity();
                newActivity.setStravaActivityId(activity.id());
                newActivity.setUser(user);
                newActivity.setName(activity.name());
                newActivity.setDistanceMeters(activity.distance());
                newActivity.setElapsedSeconds(activity.elapsedTime());
                newActivity.setAvgPace((activity.elapsedTime() / 60.0) / (activity.distance() / 1000.0));
                newActivity.setElevationGain(activity.totalElevationGain());
                newActivity.setStartedAt(activity.startDate().toLocalDateTime());
                newActivity.setActivityType(activity.type());

                activityRepository.save(newActivity);
            }
        }
    }

}
