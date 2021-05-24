package com.weather.weatherapp.dto.forecast;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ForecastDTO {

    private final String cityName;
    private final String timeZone;
    private final String forecastDate;
    private final Double temperature;
    private final Double pressure;
    private final Double humidity;
    private final Double windSpeed;
    private final Double windDegree;
}
