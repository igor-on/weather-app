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

    public String updateLocationCityName(long id, String cityName) throws JsonProcessingException {
        try {
            final Location updatedLocation = service.updateLocationCityName(id, cityName);
            return mapper.writeValueAsString(updatedLocation);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String updateLocationCoords(long id, double lat, double lon) throws JsonProcessingException {
        try {
            final Location updatedLocation = service.updateLocationCoords(id, lat, lon);
            return mapper.writeValueAsString(updatedLocation);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String updateLocationRegion(long selectedId, String region) throws JsonProcessingException {
        try {
            final Location updatedLocation = service.updateLocationRegion(selectedId, region);
            return mapper.writeValueAsString(updatedLocation);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String updateLocationCountry(long id, String country) throws JsonProcessingException {
        try {
            final Location updatedLocation = service.updateLocationCountry(id, country);
            return mapper.writeValueAsString(updatedLocation);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
