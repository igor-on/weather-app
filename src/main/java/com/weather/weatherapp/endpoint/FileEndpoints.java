package com.weather.weatherapp.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.controller.FileController;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileEndpoints {

    private final FileController fileController;
    private final HttpServer server;
    private final ObjectMapper mapper;

    public void runApp() {
        handleWriteToFile();
        handleReadFromFile();
    }

    public void handleWriteToFile() {
        server.createContext("/locations/writeToFile", exchange -> {
            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final String fileName = mapper.readTree(reqJson).get("fileName").asText();

            final String resp = fileController.writeLocationsToFile(fileName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
        server.createContext("/weathers/writeToFile", exchange -> {
            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final String fileName = mapper.readTree(reqJson).get("fileName").asText();

            final String resp = fileController.writeWeathersToFile(fileName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
    }

    public void handleReadFromFile() {
        server.createContext("/locations/readFromFile", exchange -> {
            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final String fileName = mapper.readTree(reqJson).get("fileName").asText();

            final String resp = fileController.getLocationsDataFromFile(fileName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
        server.createContext("/weathers/readFromFile", exchange -> {
            final InputStream in = exchange.getRequestBody();
            final String reqJson = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());

            final String fileName = mapper.readTree(reqJson).get("fileName").asText();

            final String resp = fileController.getWeathersDataFromFile(fileName);

            exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
            final OutputStream out = exchange.getResponseBody();
            out.write(resp.getBytes(StandardCharsets.UTF_8));
            out.close();
            exchange.close();
        });
    }
}
