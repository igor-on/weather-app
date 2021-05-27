package com.weather.weatherapp.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.controller.WeatherController;
import com.weather.weatherapp.endpoint.utils.HTTPExchangeUtils;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class WeatherEndpoints {

    private final WeatherController weatherController;
    private final HttpServer server;
    private final ObjectMapper mapper;

    public void runApp() {
        handleLocationWeatherContext();
        handleLocationForecastContext();
        handleLocationStatisticsContext();
    }

    public void handleLocationWeatherContext() {
        server.createContext("/weathers", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            String resp = null;

            if (exchange.getRequestMethod().equals("POST")) {

                final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                final JsonNode root = mapper.readTree(reqJson);

                if (root.has("cityName")) {
                    final String cityName = root.get("cityName").asText();
                    resp = weatherController.saveLocationWeatherByCityName(cityName);
                } else if (root.has("id")) {
                    final long id = root.get("id").asLong();
                    resp = weatherController.saveLocationWeatherByCoordinates(id);
                }

            } else if (exchange.getRequestMethod().equals("GET")) {
                resp = weatherController.showAllSavedWeathers();
            } else {
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                return;
            }

            assert resp != null;
            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            exchange.close();
        });
    }

    public void handleLocationForecastContext() {
        server.createContext("/forecast", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equals("POST")) {

                final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                final JsonNode root = mapper.readTree(reqJson);
                final Long id = root.get("id").asLong();
                final String selectedDate = root.get("selectedDate").asText();

                final String resp = weatherController.getLocationForecast(id, selectedDate);

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }

    public void handleLocationStatisticsContext() {
        server.createContext("/statistics", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            if (exchange.getRequestMethod().equals("GET")) {
                final String[] splitUri = exchange.getRequestURI().toString().split("/");

                if (splitUri.length > 3) {
                    exchange.sendResponseHeaders(400, 0);
                }
                final String cityName = splitUri[2];

                final String resp = weatherController.getLocationWeatherStatisticalData(cityName);

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }
}
