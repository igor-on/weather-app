package com.weather.weatherapp.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherapp.URILibrary;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.ApiOpenWeatherDTO;
import com.weather.weatherapp.dto.current.ApiWeatherStackDTO;
import com.weather.weatherapp.dto.forecast.ApiOpenWeatherForecastDTO;
import com.weather.weatherapp.dto.forecast.Forecast;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.weather.weatherapp.mapper.ForecastMapper.mapToForecast;
import static com.weather.weatherapp.mapper.WeatherMapper.mapToWeather;

public class WeatherClient {

    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    public WeatherClient() {
        httpClient = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Weather getWeatherForCityName(String cityName)
            throws IOException, InterruptedException, URISyntaxException {

        HttpResponse<String> response = sendRequestAndGetResponse(URILibrary.getCurrentWeatherForCityNameOpenWeatherURI(cityName));
        String openWeatherResponseBody = response.body();

        response = sendRequestAndGetResponse(URILibrary.getCurrentWeatherForCityNameWeatherStackURI(cityName));
        String weatherStackResponseBody = response.body();

        ApiOpenWeatherDTO apiOpenWeatherDTO = mapper.readValue(openWeatherResponseBody, ApiOpenWeatherDTO.class);
        ApiWeatherStackDTO apiWeatherStackDTO = mapper.readValue(weatherStackResponseBody, ApiWeatherStackDTO.class);

        return mapToWeather(apiOpenWeatherDTO, apiWeatherStackDTO);
    }

    public Weather getWeatherForGeographicCoordinates(double lat, double lon)
            throws IOException, InterruptedException, URISyntaxException {

        HttpResponse<String> response = sendRequestAndGetResponse(URILibrary.getCurrentWeatherForGeographicCoordinatesOpenWeatherURI(lat, lon));
        String openWeatherResponseBody = response.body();

        response = sendRequestAndGetResponse(URILibrary.getCurrentWeatherForGeographicCoordinatesWeatherStackURI(lat, lon));
        String weatherStackResponseBody = response.body();

        ApiOpenWeatherDTO apiOpenWeatherDTO = mapper.readValue(openWeatherResponseBody, ApiOpenWeatherDTO.class);
        ApiWeatherStackDTO apiWeatherStackDTO = mapper.readValue(weatherStackResponseBody, ApiWeatherStackDTO.class);

        return mapToWeather(apiOpenWeatherDTO, apiWeatherStackDTO);
    }

    public Forecast getForecastForGeographicCoordinates(double lat, double lon)
            throws IOException, InterruptedException, URISyntaxException {

        final HttpResponse<String> response = sendRequestAndGetResponse(URILibrary.getForecastForGeographicCoordinatesURI(lat, lon));

        final ApiOpenWeatherForecastDTO apiOpenWeatherForecastDTO =
                mapper.readValue(response.body(), ApiOpenWeatherForecastDTO.class);

        return mapToForecast(apiOpenWeatherForecastDTO);
    }

    private HttpResponse<String> sendRequestAndGetResponse(URI uri) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
