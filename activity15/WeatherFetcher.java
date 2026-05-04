package activity15;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Scanner;

public class WeatherFetcher {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Latitude: ");
        String lat = sc.nextLine();
        System.out.print("Enter Longitude: ");
        String lon = sc.nextLine();

        String urlString = "https://www.7timer.info/bin/astro.php?"
                + "lon=" + lon
                + "&lat=" + lat
                + "&ac=0&unit=metric&output=json";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("\n--- Weather Data Fetched Successfully ---");
                System.out.println(response.body());
            } else {
                System.out.println("Error: Received status code " + response.statusCode());
                System.out.println("Please check your coordinates and try again.");
            }
        } catch (Exception e) {
            System.out.println("A network error occurred: " + e.getMessage());

        }

        sc.close();
    }
}