package com.weather.weatherapp.dto.current;

import lombok.Getter;

@Getter
public class ApiWeatherStackDTO {

    private ApiWeatherStackLocationDTO location;
    private ApiWeatherStackCurrentDTO current;

    @Getter
    public static class ApiWeatherStackLocationDTO {
        private String name;
        private String localtime;

    }

    @Getter
    public static class ApiWeatherStackCurrentDTO {
        private Double temperature;
        private Double pressure;
        private Double humidity;
        private Double wind_speed;
        private Double wind_degree;
    }
}
