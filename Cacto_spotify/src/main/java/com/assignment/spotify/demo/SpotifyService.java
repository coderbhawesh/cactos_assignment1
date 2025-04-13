package com.assignment.spotify.demo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

    private static final String SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1";

    // Fetch Top 10 Tracks
    public String getTopTracks(String accessToken) {
        String url = SPOTIFY_API_BASE_URL + "/me/top/tracks?offset=0&limit=10";

        // Create headers with the provided access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Send GET request
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        return response;  // Return the JSON response for top 10 tracks
    }

    // Stop current track
    public String stopCurrentTrack(String accessToken) {
        String url = SPOTIFY_API_BASE_URL + "/me/player/pause";

        // Create headers with the provided access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Send POST request to stop the current playback
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return "{\"status\": \"Playback stopped\"}";
    }

    // Start playing a track by URI
    public String startTrack(String accessToken, String trackUri) {
        String url = SPOTIFY_API_BASE_URL + "/me/player/play";
        String body = "{\"uris\": [\"" + trackUri + "\"]}";

        // Create headers with the provided access token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // Send POST request to start playback
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return "{\"status\": \"Playback started\", \"trackUri\": \"" + trackUri + "\"}";
    }
}