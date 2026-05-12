package com.weather.app;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    private int timepoint;
    @SerializedName("temp")
    private int temperature;
    @SerializedName("wind")
    private Wind wind;

    public int getTimepoint() {
        return timepoint;
    }

    public int getTemperature() {
        return temperature;
    }

    public Wind getWind() {
        return wind;
    }
}
