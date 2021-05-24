package com.weather.weatherapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class StatisticalWeatherDTO {

    private String cityName;
    private Double temperature;
    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private Double windDegree;
}
