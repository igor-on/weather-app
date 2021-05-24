package com.weather.weatherapp.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherapp.URILibrary;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.ApiOpenWeatherDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

        ApiOpenWeatherDTO apiOpenWeatherDTO = mapper.readValue(openWeatherResponseBody, ApiOpenWeatherDTO.class);

        return mapToWeather(apiOpenWeatherDTO);
    }

    private HttpResponse<String> sendRequestAndGetResponse(URI uri) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
