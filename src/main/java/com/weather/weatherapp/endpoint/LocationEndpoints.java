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
        handleBetterFindLocationContext();
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
                    final String[] splitUri = exchange.getRequestURI().toString().split("/");

                    String resp;

                    if (splitUri.length > 3) {
                        exchange.sendResponseHeaders(400, 0);
                    }
                    if (splitUri.length == 2) {
                        resp = locationController.showAllSavedLocations();
                    } else {
                        final String cityName = splitUri[2];
                        resp = locationController.findLocationByCityName(cityName);
                    }


                    exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                    final OutputStream out = exchange.getResponseBody();
                    out.write(resp.getBytes(StandardCharsets.UTF_8));
                    out.close();
                    break;
                }
                case "DELETE": {
                    final String[] splitUri = exchange.getRequestURI().toString().split("/");

                    if(splitUri.length  > 3){
                        exchange.sendResponseHeaders(400, 0);
                    }
                    final long id = Long.parseLong(splitUri[2]);

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

    public void handleBetterFindLocationContext() {
        server.createContext("/location", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            final String uri = exchange.getRequestURI().toString();

            final String[] split = uri.split("/");
            if (split.length > 3) {
                exchange.sendResponseHeaders(404, 0);
            }

            final String cityName = split[2];

            System.out.println(cityName);
            String resp = locationController.findLocationByCityName(cityName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
    }
}
