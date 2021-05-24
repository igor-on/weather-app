package com.weather.weatherapp.dto.forecast;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class ApiOpenWeatherForecastDTO {

    private String timezone;
    private List<ApiOpenWeatherForecastDailyDTO> daily;

    @Getter
    public static class ApiOpenWeatherForecastDailyDTO {
        private Timestamp dt;
        //    Nie działa gdy deserializuje z API obiekty
        //    Działa gdy sam tworze obiekty
        //    @JsonUnwrapped
        private ApiOWFDailyTempDTO temp;
        private Double pressure;
        private Double humidity;
        private Double wind_speed;
        private Double wind_deg;

        public static class ApiOWFDailyTempDTO {
            public Double day;
        }

        public Timestamp getDt() {
            final long milisTimestamp = dt.getTime() * 1000;
            return new Timestamp(milisTimestamp);
        }
    }
}
