package com.weather.weatherapp.controller;

import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    private static final Long IDENTIFIER = 1L;
    private static final String CITY_NAME = "Miami";
    public static final double TEMPERATURE = 27.4;
    public static final double HUMIDITY = 54D;
    public static final double PRESSURE = 1019D;
    public static final double WIND_SPEED = 6.17;
    public static final double WIND_DEGREE = 250D;
    public static final LocalDateTime NOW = LocalDateTime.now();
    private static final Weather LOCATION_WEATHER = Weather.builder()
            .id(IDENTIFIER)
            .cityName(CITY_NAME)
            .temperature(TEMPERATURE)
            .humidity(HUMIDITY)
            .pressure(PRESSURE)
            .windSpeed(WIND_SPEED)
            .windDegree(WIND_DEGREE)
            .dateTime(NOW)
            .build();

    @Mock
    private WeatherService weatherService;
    private WeatherController controller;

    @BeforeEach
    void setUp() {
        controller = new WeatherController(weatherService);
    }

    @Test
    void thatAddSavedLocationWeatherByCityNameWorksCorrectly() throws InterruptedException, URISyntaxException, NoLocationFoundException, IOException, InvalidDataException {
        when(weatherService.getAndSaveWeatherByCityName(CITY_NAME)).thenReturn(LOCATION_WEATHER);

        final String actualJson = controller.saveLocationWeatherByCityName(CITY_NAME);

        // WeatherDTO - Json
        assertThat(actualJson).contains("\"id\" : 1,");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"temperature\" : \"27.4\",");
        assertThat(actualJson).contains("\"pressure\" : \"1019.0\",");
        assertThat(actualJson).contains("\"humidity\" : \"54.0\",");
        assertThat(actualJson).contains("\"windSpeed\" : \"6.17\",");
        assertThat(actualJson).contains("\"windDegree\" : \"250.0\"");
    }

    @Test
    void thatAddSavedLocationWeatherByCityNameThrowsNoLocationFoundException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException, InvalidDataException {
        when(weatherService.getAndSaveWeatherByCityName(CITY_NAME)).thenThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER));

        final String actualJson = controller.saveLocationWeatherByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 1\"");
    }

    @Test
    void thatAddSavedLocationWeatherByCityNameThrowsIOException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException, InvalidDataException {
        when(weatherService.getAndSaveWeatherByCityName(CITY_NAME)).thenThrow(IOException.class);

        final String actualJson = controller.saveLocationWeatherByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatAddSavedLocationWeatherByCoordinatesWorksCorrectly() throws InterruptedException, URISyntaxException, NoLocationFoundException, IOException {
        when(weatherService.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER)).thenReturn(LOCATION_WEATHER);

        final String actualJson = controller.saveLocationWeatherByCoordinates(IDENTIFIER);

        // WeatherDTO - Json
        assertThat(actualJson).contains("\"id\" : 1,");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"temperature\" : \"27.4\",");
        assertThat(actualJson).contains("\"pressure\" : \"1019.0\",");
        assertThat(actualJson).contains("\"humidity\" : \"54.0\",");
        assertThat(actualJson).contains("\"windSpeed\" : \"6.17\",");
    }

    @Test
    void thatAddSavedLocationWeatherByCoordsThrowsNoLocationException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException {
        when(weatherService.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER)).thenThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER));

        final String actualJson = controller.saveLocationWeatherByCoordinates(IDENTIFIER);

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 1\"");
    }

    @Test
    void thatAddSavedLocationWeatherByCoordsThrowsIOException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException {
        when(weatherService.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER)).thenThrow(IOException.class);

        final String actualJson = controller.saveLocationWeatherByCoordinates(IDENTIFIER);

        assertThat(actualJson).contains("\"message\" : ");
    }
}