package com.weather.weatherapp.client;

import com.weather.weatherapp.dto.Weather;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

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

}