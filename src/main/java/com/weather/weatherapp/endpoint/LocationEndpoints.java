package com.weather.weatherapp.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.endpoint.utils.HTTPExchangeUtils;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LocationEndpoints {

    private final LocationController locationController;
    private final HttpServer server;
    private final ObjectMapper mapper;

    public void runApp() {
        handleLocationContext();
    }

    public void handleLocationContext() {
        final HttpContext locationContext = server.createContext("/locations");
        locationContext.setHandler(exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            String resp = null;

            switch (exchange.getRequestMethod()) {
                case "POST": {
                    final InputStream in = exchange.getRequestBody();

                    //Zczytywanie request body(InputStream) do stringa
                    String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
                    final Location location = mapper.readValue(reqJson, Location.class);

                    //Logika programu zwracająca response
                    resp = locationController.addLocation(location.getCityName(), location.getLatitude(), location.getLongitude(), location.getRegion(), location.getCountry());

                    break;
                }
                case "GET": {
                    final String[] splitUri = exchange.getRequestURI().toString().split("/");

                    if (splitUri.length > 3) {
                        exchange.sendResponseHeaders(400, 0);
                    }
                    if (splitUri.length == 2) {
                        resp = locationController.showAllSavedLocations();
                    } else {
                        final String cityName = splitUri[2];
                        resp = locationController.findLocationByCityName(cityName);
                    }
                    break;
                }
                case "DELETE": {
                    final String[] splitUri = exchange.getRequestURI().toString().split("/");

                    if (splitUri.length > 3) {
                        exchange.sendResponseHeaders(400, 0);
                    }
                    final long id = Long.parseLong(splitUri[2]);

                    resp = locationController.removeLocation(id);
                    break;
                }
                case "PUT": {


                    final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                    final JsonNode root = mapper.readTree(reqJson);
                    final long id = root.get("id").asLong();

                    if (root.has("cityName")) {

                        final String cityName = root.get("cityName").asText();
                        resp = locationController.updateLocationCityName(id, cityName);

                    } else if (root.has("latitude") && root.has("longitude")) {

                        final double lat = root.get("latitude").asDouble();
                        final double lon = root.get("longitude").asDouble();
                        resp = locationController.updateLocationCoords(id, lat, lon);

                    } else if (root.has("region")) {

                        final String region = root.get("region").asText();
                        resp = locationController.updateLocationRegion(id, region);

                    } else if (root.has("country")) {

                        final String country = root.get("country").asText();
                        resp = locationController.updateLocationCountry(id, country);
                    }
                    break;
                }
                default:
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
}
