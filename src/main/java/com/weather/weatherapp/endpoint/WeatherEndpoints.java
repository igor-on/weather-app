package com.weather.weatherapp.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.controller.WeatherController;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WeatherEndpoints {

    private final WeatherController weatherController;
    private final HttpServer server;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    public void runApp() {
        handleLocationWeatherContext();
        handleLocationForecastContext();
        handleLocationStatisticsContext();
    }

    public void handleLocationWeatherContext() {
        server.createContext("/locationWeather", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equals("POST")) {

                final InputStream in = exchange.getRequestBody();
                final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

                final JsonNode root = mapper.readTree(reqJson);

                String resp = null;

                if (root.has("cityName")) {
                    final String cityName = root.get("cityName").asText();
                    resp = weatherController.saveLocationWeatherByCityName(cityName);
                } else if (root.has("id")) {
                    final long id = root.get("id").asLong();
                    resp = weatherController.saveLocationWeatherByCoordinates(id);
                }

                assert resp != null;
                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                final OutputStream out = exchange.getResponseBody();
                out.write(resp.getBytes(StandardCharsets.UTF_8));
                out.close();
            }
            if (exchange.getRequestMethod().equals("GET")) {

                final String resp = weatherController.showAllSavedWeathers();

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                final OutputStream out = exchange.getResponseBody();
                out.write(resp.getBytes(StandardCharsets.UTF_8));
                out.close();
            }

            exchange.close();
        });
    }

    public void handleLocationForecastContext() {
        server.createContext("/locationForecast", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final JsonNode root = mapper.readTree(reqJson);
            final Long id = root.get("id").asLong();
            final String selectedDate = root.get("selectedDate").asText();

            final String resp = weatherController.getLocationForecast(id, selectedDate);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
    }

    public void handleLocationStatisticsContext(){
        server.createContext("/location/statistics", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final JsonNode root = mapper.readTree(reqJson);
            final String cityName = root.get("cityName").asText();

            final String resp = weatherController.getLocationWeatherStatisticalData(cityName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
    }
}
