package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weatherapp.dto.StatisticalWeatherDTO;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.forecast.Forecast;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    private static final Long IDENTIFIER = 1L;
    private static final String CITY_NAME = "Miami";
    private static final double TEMPERATURE = 27.4;
    private static final double HUMIDITY = 54D;
    private static final double PRESSURE = 1019D;
    private static final double WIND_SPEED = 6.17;
    private static final double WIND_DEGREE = 250D;
    private static final LocalDateTime NOW = LocalDateTime.now();
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
    private static final Forecast.ForecastDay FORECAST_DAY = Forecast.ForecastDay.builder()
            .forecastDate(LocalDate.now().plusDays(1))
            .temperature(27.26)
            .pressure(1020D)
            .humidity(62D)
            .windSpeed(7.05)
            .windDegree(101D)
            .build();
    private static final Forecast LOCATION_FORECAST = Forecast.builder()
            .cityName(CITY_NAME)
            .timezone("America/New_York")
            .build();
    private static final StatisticalWeatherDTO LOCATION_STATISTICAL_DATA_DTO = StatisticalWeatherDTO.builder()
            .cityName(CITY_NAME)
            .temperature(TEMPERATURE)
            .pressure(PRESSURE)
            .humidity(HUMIDITY)
            .windSpeed(WIND_SPEED)
            .windDegree(WIND_DEGREE)
            .build();

    @Mock
    private WeatherService weatherService;
    private WeatherController controller;

    @BeforeEach
    void setUp() {
        controller = new WeatherController(weatherService);
    }

    @Test
    void thatSaveLocationWeatherByCityNameWorksCorrectly() throws InterruptedException, URISyntaxException, NoLocationFoundException, IOException, InvalidDataException {
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
    void thatSaveLocationWeatherByCityNameThrowsNoLocationFoundException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException, InvalidDataException {
        when(weatherService.getAndSaveWeatherByCityName(CITY_NAME)).thenThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER));

        final String actualJson = controller.saveLocationWeatherByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 1\"");
    }

    @Test
    void thatSaveLocationWeatherByCityNameThrowsIOException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException, InvalidDataException {
        when(weatherService.getAndSaveWeatherByCityName(CITY_NAME)).thenThrow(IOException.class);

        final String actualJson = controller.saveLocationWeatherByCityName(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatSaveLocationWeatherByCoordinatesWorksCorrectly() throws InterruptedException, URISyntaxException, NoLocationFoundException, IOException {
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
    void thatSaveLocationWeatherByCoordsThrowsNoLocationException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException {
        when(weatherService.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER)).thenThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER));

        final String actualJson = controller.saveLocationWeatherByCoordinates(IDENTIFIER);

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 1\"");
    }

    @Test
    void thatSaveLocationWeatherByCoordsThrowsIOException() throws IOException, InterruptedException, URISyntaxException, NoLocationFoundException {
        when(weatherService.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER)).thenThrow(IOException.class);

        final String actualJson = controller.saveLocationWeatherByCoordinates(IDENTIFIER);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatShowAllSavedWeathersWorksCorrectly() throws JsonProcessingException {
        List<Weather> list = new ArrayList<>();
        list.add(LOCATION_WEATHER);
        when(weatherService.getAllLocationsWeathers()).thenReturn(list);

        final String actualJson = controller.showAllSavedWeathers();

        // WeatherDTO - Json
        assertThat(actualJson).contains("\"id\" : 1,");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"temperature\" : \"27.4\",");
        assertThat(actualJson).contains("\"pressure\" : \"1019.0\",");
        assertThat(actualJson).contains("\"humidity\" : \"54.0\",");
        assertThat(actualJson).contains("\"windSpeed\" : \"6.17\",");
    }

    @Test
    void thatGetLocationForecastWorksCorrectly() throws InterruptedException, IOException, NoLocationFoundException, URISyntaxException, InvalidDataException {
        List<Forecast.ForecastDay> forecastDayList = new ArrayList<>();
        forecastDayList.add(FORECAST_DAY);
        LOCATION_FORECAST.setForecastDaysList(forecastDayList);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, E");
        final String dateString = NOW.plusDays(1).format(formatter);

        when(weatherService.getWeatherForecast(IDENTIFIER, dateString)).thenReturn(LOCATION_FORECAST);

        final String actualJson = controller.getLocationForecast(IDENTIFIER, dateString);

        //ForecastDTO - Json
        assertThat(actualJson).doesNotContain("\"id\" : ");
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"timeZone\" : \"America/New_York\",");
        assertThat(actualJson).contains("\"forecastDate\" : \"" + dateString + "\",");
        assertThat(actualJson).contains("\"temperature\" : 27.26,");
        assertThat(actualJson).contains("\"pressure\" : 1020.0,");
        assertThat(actualJson).contains("\"humidity\" : 62.0,");
        assertThat(actualJson).contains("\"windSpeed\" : 7.05,");
    }

    @Test
    void thatGetLocationForecastThrowsNoLocationFoundException() throws IOException, InterruptedException, NoLocationFoundException, URISyntaxException, InvalidDataException {
        when(weatherService.getWeatherForecast(IDENTIFIER, NOW.toString())).thenThrow(new NoLocationFoundException("There is no location with given id: " + IDENTIFIER));

        final String actualJson = controller.getLocationForecast(IDENTIFIER, NOW.toString());

        assertThat(actualJson).contains("\"message\" : \"There is no location with given id: 1\"");
    }

    @Test
    void thatGetLocationForecastThrowsIOException() throws InterruptedException, IOException, NoLocationFoundException, URISyntaxException, InvalidDataException {
        when(weatherService.getWeatherForecast(IDENTIFIER, NOW.toString())).thenThrow(IOException.class);

        final String actualJson = controller.getLocationForecast(IDENTIFIER, NOW.toString());

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatGetLocationForecastThrowsInvalidDataException() throws InterruptedException, IOException, NoLocationFoundException, URISyntaxException, InvalidDataException {
        when(weatherService.getWeatherForecast(IDENTIFIER, NOW.toString())).thenThrow(InvalidDataException.class);

        final String actualJson = controller.getLocationForecast(IDENTIFIER, NOW.toString());

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void getLocationWeatherStatisticalData() throws InvalidDataException, JsonProcessingException, NoLocationFoundException {
        when(weatherService.getStatisticalData(CITY_NAME)).thenReturn(LOCATION_STATISTICAL_DATA_DTO);

        final String actualJson = controller.getLocationWeatherStatisticalData(CITY_NAME);

        //StatisticalWeatherDTO - Json
        assertThat(actualJson).contains("\"cityName\" : \"Miami\",");
        assertThat(actualJson).contains("\"temperature\" : 27.4,");
        assertThat(actualJson).contains("\"pressure\" : 1019.0,");
        assertThat(actualJson).contains("\"humidity\" : 54.0,");
        assertThat(actualJson).contains("\"windSpeed\" : 6.17,");
        assertThat(actualJson).contains("\"windDegree\" : 250.0");
        assertThat(actualJson).doesNotContain("\"dateTime\" : ");
    }

    @Test
    void thatGetLocationWeatherStatisticalDataThrowsInvalidDataException() throws JsonProcessingException, InvalidDataException, NoLocationFoundException {
        when(weatherService.getStatisticalData(CITY_NAME)).thenThrow(new InvalidDataException("There can't be more than one result"));

        final String actualJson = controller.getLocationWeatherStatisticalData(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : \"There can't be more than one result\"");
    }

    @Test
    void thatGetLocationWeatherStatisticalDataThrowsNoLocationFoundException() throws JsonProcessingException, InvalidDataException, NoLocationFoundException {
        when(weatherService.getStatisticalData(CITY_NAME)).thenThrow(new NoLocationFoundException("There is no saved location with given city name: " + CITY_NAME));

        final String actualJson = controller.getLocationWeatherStatisticalData(CITY_NAME);

        assertThat(actualJson).contains("\"message\" : \"There is no saved location with given city name: Miami\"");
    }
}