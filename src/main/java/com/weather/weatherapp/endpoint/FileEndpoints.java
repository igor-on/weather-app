package com.weather.weatherapp.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.controller.FileController;
import com.weather.weatherapp.endpoint.utils.HTTPExchangeUtils;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

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
            if (exchange.getRequestMethod().equals("POST")) {

                final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                final String fileName = mapper.readTree(reqJson).get("fileName").asText();

                final String resp = fileController.writeLocationsToFile(fileName);

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
        server.createContext("/weathers/writeToFile", exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {

                final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                final String fileName = mapper.readTree(reqJson).get("fileName").asText();

                final String resp = fileController.writeWeathersToFile(fileName);

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }

    public void handleReadFromFile() {
        server.createContext("/locations/readFromFile", exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {

                final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                final String fileName = mapper.readTree(reqJson).get("fileName").asText();

                final String resp = fileController.getLocationsDataFromFile(fileName);

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
        server.createContext("/weathers/readFromFile", exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {

                final String reqJson = HTTPExchangeUtils.convertRequestBodyToString(exchange);

                final String fileName = mapper.readTree(reqJson).get("fileName").asText();

                final String resp = fileController.getWeathersDataFromFile(fileName);

                exchange.sendResponseHeaders(200, resp.getBytes(StandardCharsets.UTF_8).length);
                HTTPExchangeUtils.sendResponseBodyToUser(exchange, resp);
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        });
    }
}
