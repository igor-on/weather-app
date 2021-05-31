package com.weather.weatherapp.mapper;

import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.WeatherDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherDTOMapper {


    public static WeatherDTO mapToWeatherDTO(Weather locationWeather) {

        final LocalDateTime locationWeatherDateTime = locationWeather.getDateTime();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm");
        String stringDateTime = locationWeatherDateTime.format(formatter);

        return WeatherDTO.builder()
                .id(locationWeather.getId())
                .cityName(locationWeather.getCityName())
                .temperature(String.valueOf(locationWeather.getTemperature()))
                .humidity(String.valueOf(locationWeather.getHumidity()))
                .pressure(String.valueOf(locationWeather.getPressure()))
                .windSpeed(String.valueOf(locationWeather.getWindSpeed()))
                .windDegree(String.valueOf(locationWeather.getWindDegree()))
                .dateTime(stringDateTime)
                .build();
    }
}
