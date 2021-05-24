package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.service.LocationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    ObjectMapper mapper = new ObjectMapper();
    private static final Long IDENTIFIER = 2L;
    public static final String CITY_NAME = "Miami";
    public static final Double LATITUDE = 25.7743;
    public static final Double LONGITUDE = -80.1937;
    public static final String REGION = null;
    public static final String COUNTRY = "US";
    private static final Location LOCATION = new Location(IDENTIFIER, CITY_NAME, LATITUDE, LONGITUDE, REGION, COUNTRY);

    @Mock
    LocationService locationService;
    private LocationController controller;

    @BeforeEach
    void setUp() {
        controller = new LocationController(locationService);
    }

    @AfterEach
    void set() {
        LOCATION.setId(IDENTIFIER);
        LOCATION.setCityName(CITY_NAME);
        LOCATION.setLatitude(LATITUDE);
        LOCATION.setLongitude(LONGITUDE);
        LOCATION.setRegion(REGION);
        LOCATION.setCountry(COUNTRY);
    }

    @Test
    void thatAddLocationWorksCorrectly() throws InvalidDataException, JsonProcessingException {
        when(locationService.createAndSaveLocation(CITY_NAME, LATITUDE, LONGITUDE, REGION, COUNTRY)).thenReturn(LOCATION);

        final String actualJson = controller.addLocation(CITY_NAME, LATITUDE, LONGITUDE, REGION, COUNTRY);

        assertThat(actualJson).contains("\"id\" : 2,");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"region\" : null,");
        assertThat(actualJson).contains("\"latitude\" : 25.7743,");
        assertThat(actualJson).contains("\"country\" : \"US\"");
    }

    @Test
    void thatAddLocationThrowsInvalidDataException() throws Exception {
        when(locationService.createAndSaveLocation(null, LATITUDE, LONGITUDE, REGION, COUNTRY)).thenThrow(new InvalidDataException("City name can't be empty"));

        final String actualJson = controller.addLocation(null, LATITUDE, LONGITUDE, REGION, COUNTRY);

        assertThat(actualJson).contains("\"message\" : \"City name can't be empty\"");
    }

    @Test
    void thatRemoveLocationWorksCorrectly() throws NoLocationFoundException, JsonProcessingException {
        doNothing().when(locationService).removeLocation(IDENTIFIER);

        final String actual = controller.removeLocation(IDENTIFIER);

        assertThat(actual).isEqualTo("Location deleted correctly");
        verify(locationService, times(1)).removeLocation(IDENTIFIER);
    }

    @Test
    void thatRemoveLocationThrowsNoLocationFoundException() throws NoLocationFoundException, JsonProcessingException {
        doThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER)).when(locationService).removeLocation(IDENTIFIER);

        final String actualJson = controller.removeLocation(IDENTIFIER);

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 2\"");
        verify(locationService, times(1)).removeLocation(IDENTIFIER);
    }

    @Test
    void thatFindLocationByCityNameWorksCorrectly() throws NoLocationFoundException, JsonProcessingException, InvalidDataException {
        when(locationService.findLocation(CITY_NAME)).thenReturn(LOCATION);

        final String actualJson = controller.findLocationByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"id\" : 2,");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"region\" : null,");
        assertThat(actualJson).contains("\"latitude\" : 25.7743,");
        assertThat(actualJson).contains("\"country\" : \"US\"");
    }

    @Test
    void thatFindLocationByCityNameThrowsInvalidDataException() throws JsonProcessingException, NoLocationFoundException, InvalidDataException {
        when(locationService.findLocation(CITY_NAME)).thenThrow(new InvalidDataException("Given city name is not correct"));

        final String actualJson = controller.findLocationByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : \"Given city name is not correct\"");
    }

    @Test
    void thatFindLocationByCityNameThrowsNoLocationFoundException() throws JsonProcessingException, NoLocationFoundException, InvalidDataException {
        when(locationService.findLocation(CITY_NAME)).thenThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER));

        final String actualJson = controller.findLocationByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 2\"");
    }

    @Test
    void thatShowAllSavedLocationsWorksCorrectly() throws IOException {
        List<Location> locationList = new ArrayList<>();
        locationList.add(LOCATION);
        locationList.add(LOCATION);
        when(locationService.getAllLocations()).thenReturn(locationList);

        final String actualJson = controller.showAllSavedLocations();
        final List<Location> list = List.of(mapper.readValue(actualJson, Location[].class));

        assertThat(list).hasSize(2);
        assertThat(actualJson).contains("\"id\" : 2,");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"region\" : null,");
        assertThat(actualJson).contains("\"latitude\" : 25.7743,");
        assertThat(actualJson).contains("\"country\" : \"US\"");
    }
}