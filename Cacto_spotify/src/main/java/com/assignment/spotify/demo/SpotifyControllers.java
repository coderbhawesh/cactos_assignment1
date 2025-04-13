package com.assignment.spotify.demo;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RestController
public class SpotifyControllers {

    private final SpotifyService spotifyService;

    private final String CLIENT_ID = "$$";
    private final String CLIENT_SECRET = "##";
    private final String REDIRECT_URI = "http://localhost:8080/callback";
    private final String SPOTIFY_TOKEN_URL = "https://accounts.spotify.com/api/token";

    public SpotifyControllers(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/callback")
    public String handleSpotifyCallback(@RequestParam String code) {
        // Exchange the authorization code for an access token
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("access_code", code);
        body.add("redirect_uri", REDIRECT_URI);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(SPOTIFY_TOKEN_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();  // The access token will be in this response
    }

    // Endpoint to get top 10 tracks and now playing
    @GetMapping("/spotify")
    public String getTopTracks(@RequestParam String accessToken) {
        return spotifyService.getTopTracks(accessToken);
    }

    // Endpoint to stop current song
    @GetMapping("/spotify/stop")
    public String stopCurrentSong(@RequestParam String accessToken) {
        return spotifyService.stopCurrentTrack(accessToken);
    }

    // Endpoint to start playing a song by URI
    @GetMapping("/spotify/play")
    public String playSong(@RequestParam String accessToken, @RequestParam String trackUri) {
        return spotifyService.startTrack(trackUri, accessToken);
    }
}
