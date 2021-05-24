package com.weather.weatherapp.dto.forecast;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class Forecast {

    private String cityName;
    private String timezone;
    private List<ForecastDay> forecastDaysList;

    @Getter
    @Builder
    public static class ForecastDay {

        private LocalDate forecastDate;
        private Double temperature;
        private Double pressure;
        private Double humidity;
        private Double windSpeed;
        private Double windDegree;
    }
}
