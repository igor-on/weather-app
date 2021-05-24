package com.weather.weatherapp.dto.current;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherDTO {

    private final Long id;
    private final String cityName;
    private final String temperature;
    private final String humidity;
    private final String pressure;
    private final String windSpeed;
    private final String windDegree;
    private final String dateTime;
}
