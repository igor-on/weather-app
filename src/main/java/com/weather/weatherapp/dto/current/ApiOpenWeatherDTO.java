package com.weather.weatherapp.dto.current;

import lombok.Getter;

@Getter
public class ApiOpenWeatherDTO {

    private ApiOpenWeatherMainDTO main;
    private ApiOpenWeatherWindDTo wind;
    private String name;


    @Getter
    public static class ApiOpenWeatherMainDTO {
        private Double temp;
        private Double pressure;
        private Double humidity;
    }

    @Getter
    public static class ApiOpenWeatherWindDTo {
        private Double speed;
        private Double deg;
    }

}
