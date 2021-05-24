package com.weather.weatherapp.mapper;

import com.weather.weatherapp.dto.forecast.ApiOpenWeatherForecastDTO;
import com.weather.weatherapp.dto.forecast.Forecast;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class ForecastMapper {

    public static Forecast mapToForecast(ApiOpenWeatherForecastDTO forecastDTO) {
        final List<Forecast.ForecastDay> forecastDays = forecastDTO.getDaily().stream()
                .map(e -> mapToForecastDay(e))
                .collect(Collectors.toList());

        return Forecast.builder()
                .timezone(forecastDTO.getTimezone())
                .forecastDaysList(forecastDays)
                .build();
    }

    private static Forecast.ForecastDay mapToForecastDay(ApiOpenWeatherForecastDTO.ApiOpenWeatherForecastDailyDTO forecastDayDTO) {
        final LocalDate localDate = forecastDayDTO.getDt().toInstant().atZone(ZoneOffset.UTC).toLocalDate();

        return Forecast.ForecastDay.builder()
                .forecastDate(localDate)
                .temperature(forecastDayDTO.getTemp().day)
                .humidity(forecastDayDTO.getHumidity())
                .pressure(forecastDayDTO.getPressure())
                .windSpeed(forecastDayDTO.getWind_speed())
                .windDegree(forecastDayDTO.getWind_deg())
                .build();
    }
}
