package com.weather.weatherapp;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class URILibrary {

    private static final String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String FIRST_API_KEY = "";
    private static final String WEATHER_STACK_URL = "http://api.weatherstack.com/current";
    private static final String SECOND_API_KEY = "";

    public static URI getCurrentWeatherForCityNameOpenWeatherURI(String cityName) throws URISyntaxException {
        return new URIBuilder(OPEN_WEATHER_URL)
                .addParameter("q", cityName)
                .addParameter("appid", FIRST_API_KEY)
                .addParameter("units", "metric")
                .addParameter("lang", "pl")
                .build();
    }

    public static URI getCurrentWeatherForCityNameWeatherStackURI(String cityName) throws URISyntaxException {
        return new URIBuilder(WEATHER_STACK_URL)
                .addParameter("access_key", SECOND_API_KEY)
                .addParameter("query", cityName)
                .addParameter("units", "m")
                .build();
    }

    public static URI getCurrentWeatherForGeographicCoordinatesOpenWeatherURI(double lat, double lon) throws URISyntaxException {
        return new URIBuilder(OPEN_WEATHER_URL)
                .addParameter("lat", String.valueOf(lat))
                .addParameter("lon", String.valueOf(lon))
                .addParameter("appid", FIRST_API_KEY)
                .addParameter("units", "metric")
                .addParameter("lang", "pl")
                .build();
    }

    public static URI getCurrentWeatherForGeographicCoordinatesWeatherStackURI(double lat, double lon) throws URISyntaxException {
        return new URIBuilder(WEATHER_STACK_URL)
                .addParameter("access_key", SECOND_API_KEY)
                .addParameter("query", String.format("%s,%s", lat, lon))
                .addParameter("units", "m")
                .build();
    }
}
