package com.weather.weatherapp;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class URILibrary {

    private static final String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String FIRST_API_KEY = "";

    public static URI getCurrentWeatherForCityNameOpenWeatherURI(String cityName) throws URISyntaxException {
        return new URIBuilder(OPEN_WEATHER_URL)
                .addParameter("q", cityName)
                .addParameter("appid", FIRST_API_KEY)
                .addParameter("units", "metric")
                .addParameter("lang", "pl")
                .build();
    }
}
