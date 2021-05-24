package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.service.LocationService;
import com.weather.weatherapp.exception.Error;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    public String removeLocation(long id) throws JsonProcessingException {
        try {
            service.removeLocation(id);
            return "Location deleted correctly";
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String findLocationByCityName(String cityName) throws JsonProcessingException {
        try {
            Location location = service.findLocation(cityName);
            return mapper.writeValueAsString(location);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String showAllSavedLocations() throws JsonProcessingException {
        try {
            List<Location> allLocations = service.getAllLocations();
            return mapper.writeValueAsString(allLocations);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
