package com.weather.weatherapp.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.dto.Location;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LocationEndpoints {

    private final LocationController locationController;
    private final HttpServer server;
    private final HttpContext locationContext;
    private final ObjectMapper mapper;

    public LocationEndpoints(LocationController locationController, HttpServer server, ObjectMapper mapper) {
        this.locationController = locationController;
        this.server = server;
        this.mapper = mapper;
        locationContext = server.createContext("/locations");
    }

    public void runApp() throws JsonProcessingException {
        handleLocationContext();
        handleFindLocationContext();
    }

    public void handleLocationContext() throws JsonProcessingException {
        locationContext.setHandler(exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            switch (exchange.getRequestMethod()) {
                case "POST": {
                    final InputStream in = exchange.getRequestBody();

                    //Zczytywanie request body(InputStream) do stringa
                    String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
                    final Location location = mapper.readValue(reqJson, Location.class);

                    //Logika programu zwracająca response
                    final String resp = locationController.addLocation(location.getCityName(), location.getLatitude(), location.getLongitude(), location.getRegion(), location.getCountry());

                    exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                    final OutputStream out = exchange.getResponseBody();
                    out.write(resp.getBytes(StandardCharsets.UTF_8));
                    out.close();
                    break;
                }
                case "GET": {

                    final String resp = locationController.showAllSavedLocations();

                    exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                    final OutputStream out = exchange.getResponseBody();
                    out.write(resp.getBytes(StandardCharsets.UTF_8));
                    out.close();
                    break;
                }
                case "DELETE": {

                    final InputStream in = exchange.getRequestBody();
                    String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

                    final JsonNode root = mapper.readTree(reqJson);
                    final long id = root.get("id").asLong();

                    final String resp = locationController.removeLocation(id);

                    exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                    final OutputStream out = exchange.getResponseBody();
                    out.write(resp.getBytes(StandardCharsets.UTF_8));
                    out.close();
                    break;
                }
                case "PUT": {

                    final InputStream in = exchange.getRequestBody();
                    final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

                    final JsonNode root = mapper.readTree(reqJson);
                    final long id = root.get("id").asLong();

                    String resp = null;

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

                    assert resp != null;
                    exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                    final OutputStream out = exchange.getResponseBody();
                    out.write(resp.getBytes(StandardCharsets.UTF_8));
                    out.close();
                    break;
                }
                default:
                    exchange.sendResponseHeaders(405, 0);
                    break;
            }
            exchange.close();
        });
    }

    public void handleFindLocationContext() {
        final HttpContext context = server.createContext("/findLocation");
//        final HttpContext context = server.createContext(String.format("location/&s", String.class));

        context.setHandler(exchange -> {

            if(exchange.getRequestMethod().equals("POST")){

            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final JsonNode root = mapper.readTree(reqJson);
            final String cityName = root.get("cityName").asText();

            final String resp = locationController.findLocationByCityName(cityName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }
}
