package com.weather.app;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Latitude: ");
        String latStr = sc.nextLine();
        System.out.print("Enter Longitude: ");
        String lonStr = sc.nextLine();

        try {
            double lat = Double.parseDouble(latStr);
            double lon = Double.parseDouble(lonStr);

            WeatherService weatherService = new WeatherService();
            WeatherResponse response = weatherService.getForecast(lat, lon);

            if (response != null && response.getForecastSeries() != null && !response.getForecastSeries().isEmpty()) {
                System.out.println("\n--- Weather Data Fetched Successfully ---");
                List<Forecast> forecasts = response.getForecastSeries();

                System.out.println("Displaying first 3 forecast entries:");
                for (int i = 0; i < Math.min(3, forecasts.size()); i++) {
                    Forecast forecast = forecasts.get(i);
                    // Defensive check for wind data
                    if (forecast.getWind() != null) {
                        System.out.printf("At hour %d: %d°C with %d speed winds from the %s.%n",
                                forecast.getTimepoint(),
                                forecast.getTemperature(),
                                forecast.getWind().getSpeed(),
                                forecast.getWind().getDirection());
                    } else {
                        System.out.printf("At hour %d: %d°C (wind data not available).%n",
                                forecast.getTimepoint(),
                                forecast.getTemperature());
                    }
                }
            } else {
                System.out.println("Could not retrieve weather data or data was empty.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid latitude or longitude. Please enter numeric values.");
        } catch (InterruptedException e) {
            System.out.println("A network error occurred: " + e.getMessage());
            System.out.println("Could not retrieve weather data.");
        } catch (IOException e) {
            System.out.println("An IO error occurred: " + e.getMessage());
            System.out.println("Could not retrieve weather data.");
        } finally {
            sc.close();
        }
    }
}
