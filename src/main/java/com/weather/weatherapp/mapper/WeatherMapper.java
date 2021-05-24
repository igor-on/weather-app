package com.weather.weatherapp.mapper;

import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.ApiOpenWeatherDTO;

import java.time.LocalDateTime;

public class WeatherMapper {

    public static Weather mapToWeather(ApiOpenWeatherDTO openWeatherDTO) {

        return Weather.builder()
                .cityName(openWeatherDTO.getName())
                .temperature(openWeatherDTO.getMain().getTemp())
                .humidity(openWeatherDTO.getMain().getHumidity())
                .pressure(openWeatherDTO.getMain().getPressure())
                .windSpeed(openWeatherDTO.getWind().getSpeed())
                .windDegree(openWeatherDTO.getWind().getDeg())
                .dateTime(LocalDateTime.now())
                .build();
    }
}
