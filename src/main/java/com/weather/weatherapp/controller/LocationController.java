package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.service.LocationService;
import com.weather.weatherapp.exception.Error;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    public String addLocation(String cityName, double lat, double lon, String region, String country) throws JsonProcessingException {
        try {
            final Location location = service.createAndSaveLocation(cityName, lat, lon, region, country);
            return mapper.writeValueAsString(location);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
