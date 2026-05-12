package com.weather.app;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    private final HttpClient httpClient;
    private final Gson gson;

    public WeatherService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public WeatherResponse getForecast(double lat, double lon) throws IOException, InterruptedException {
        String urlString = "https://www.7timer.info/bin/astro.php?lon=" + lon + "&lat=" + lat
                + "&ac=0&unit=metric&output=json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), WeatherResponse.class);
        } else {
            System.err.println("Error: Received status code " + response.statusCode());
            throw new IOException("Failed to fetch weather data. Status code: " + response.statusCode());
        }
    }
}
