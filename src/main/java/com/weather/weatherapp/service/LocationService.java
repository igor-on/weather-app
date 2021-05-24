package com.weather.weatherapp.service;

import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.repository.LocationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createAndSaveLocation(String cityName, double lat, double lon, String region, String country) throws InvalidDataException {
        listenForExceptions(cityName, lat, lon, country);

        cityName = cityName.trim();
        country = country.trim();

        Location createdLocation = new Location(null, cityName, lat, lon, country);

        if (region != null && !region.isBlank()) {
            region = region.trim();
            createdLocation.setRegion(region);
        }

        return locationRepository.save(createdLocation);
    }

    private void listenForExceptions(String cityName, double lat, double lon, String country) throws InvalidDataException {
        if (stringValueIsNotValid(cityName)) {
            throw new InvalidDataException("City name can't be empty");
        }
        if (stringValueIsNotValid(country)) {
            throw new InvalidDataException("Given country is not correct");
        }
        if (coordinatesNotValid(lat, lon)) {
            throw new InvalidDataException("Given geographical coordinates aren't correct");
        }
    }

    public boolean stringValueIsNotValid(String value) {
        return value == null || value.isBlank();
    }

    boolean coordinatesNotValid(double lat, double lon) {
        return (lat < -90 || lat > 90) || (lon < -180 || lon > 180);
    }
}
