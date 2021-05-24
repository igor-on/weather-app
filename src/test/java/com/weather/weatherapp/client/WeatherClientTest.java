package com.weather.weatherapp.client;

import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.forecast.Forecast;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherClientTest {
    private static final double MIAMI_LAT = 25.7743;
    private static final double MIAMI_LON = -80.1937;
    private final WeatherClient client = new WeatherClient();

    @Test
    void thatGetWeatherForCityNameWorksCorrectly() throws InterruptedException, IOException, URISyntaxException {

        final Weather actual = client.getWeatherForCityName("Miami");

        assertThat(actual.getId()).isNull();
        assertThat(actual.getCityName()).isNotNull().isEqualTo("Miami");
        assertThat(actual.getTemperature()).isNotNull();
        assertThat(actual.getHumidity()).isNotNull();
        assertThat(actual.getPressure()).isNotNull();
        assertThat(actual.getWindSpeed()).isNotNull();
        assertThat(actual.getWindDegree()).isNotNull();
        assertThat(actual.getDateTime()).isNotNull();
    }

    @Test
    void thatGetWeatherForGeographicCoordinatesWorksCorrectly() throws InterruptedException, IOException, URISyntaxException {

        final Weather actual = client.getWeatherForGeographicCoordinates(MIAMI_LAT, MIAMI_LON);

        assertThat(actual.getId()).isNull();
        assertThat(actual.getCityName()).isNotNull().isEqualTo("Miami");
        assertThat(actual.getTemperature()).isNotNull();
        assertThat(actual.getHumidity()).isNotNull();
        assertThat(actual.getPressure()).isNotNull();
        assertThat(actual.getWindSpeed()).isNotNull();
        assertThat(actual.getWindDegree()).isNotNull();
        assertThat(actual.getDateTime()).isNotNull();
    }

    @Test
    void getForecastForGeographicCoordinates() throws InterruptedException, IOException, URISyntaxException {
        final String stringDate = LocalDate.now().plusDays(1).toString();

        final Forecast actual = client.getForecastForGeographicCoordinates(MIAMI_LAT, MIAMI_LON);

        assertThat(actual.getCityName()).isNull();
        assertThat(actual.getTimezone()).isNotNull().isEqualTo("America/New_York");
        assertThat(actual.getForecastDaysList().size()).isEqualTo(8);
        assertThat(actual.getForecastDaysList().get(1).getForecastDate()).isNotNull().isEqualTo(stringDate);
        assertThat(actual.getForecastDaysList().get(1).getTemperature()).isNotNull();
        assertThat(actual.getForecastDaysList().get(1).getHumidity()).isNotNull();
        assertThat(actual.getForecastDaysList().get(1).getPressure()).isNotNull();
        assertThat(actual.getForecastDaysList().get(1).getWindSpeed()).isNotNull();
        assertThat(actual.getForecastDaysList().get(1).getWindDegree()).isNotNull();
    }
}