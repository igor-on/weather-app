package com.weather.weatherapp.endpoint.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HTTPExchangeUtils {

    public static String convertRequestBodyToString(HttpExchange exchange) {
        final InputStream in = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining());
    }

    public static void sendResponseBodyToUser(HttpExchange exchange, String resp) throws IOException {
        final OutputStream out = exchange.getResponseBody();
        out.write(resp.getBytes(StandardCharsets.UTF_8));
        out.close();
    }
}
