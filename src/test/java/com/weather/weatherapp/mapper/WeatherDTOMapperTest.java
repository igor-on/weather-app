package com.weather.weatherapp.mapper;

import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.WeatherDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherDTOMapperTest {

    private static final Weather LOCATION_WEATHER = Weather.builder()
            .id(1L)
            .cityName("Miami")
            .temperature(27.4)
            .humidity(54D)
            .pressure(1019D)
            .windSpeed(6.17)
            .windDegree(250D)
            .dateTime(LocalDateTime.now())
            .build();

    @Test
    void mapToLocationWeatherDTO() {

        final WeatherDTO actual = WeatherDTOMapper.mapToWeatherDTO(LOCATION_WEATHER);

        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getCityName()).isEqualTo("Miami");
        assertThat(actual.getTemperature()).isEqualTo("27.4");
        assertThat(actual.getHumidity()).isEqualTo("54.0");
        assertThat(actual.getPressure()).isEqualTo("1019.0");
        assertThat(actual.getWindSpeed()).isEqualTo("6.17");
        assertThat(actual.getWindDegree()).isEqualTo("250.0");
    }
}