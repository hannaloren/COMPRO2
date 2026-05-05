package com.weather.app;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    private String product;
    @SerializedName("dataseries")
    private List<Forecast> forecastSeries;

    public String getProduct() {
        return product;
    }

    public List<Forecast> getForecastSeries() {
        return forecastSeries;
    }
}
