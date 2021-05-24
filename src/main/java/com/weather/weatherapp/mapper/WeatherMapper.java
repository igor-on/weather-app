package com.weather.weatherapp.mapper;

import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.ApiOpenWeatherDTO;
import com.weather.weatherapp.dto.current.ApiWeatherStackDTO;

import java.time.LocalDateTime;

public class WeatherMapper {

    public static Weather mapToWeather(ApiOpenWeatherDTO openWeatherDTO, ApiWeatherStackDTO weatherStackDTO) {

        final double avgTemp = (openWeatherDTO.getMain().getTemp() + weatherStackDTO.getCurrent().getTemperature()) / 2;
        final double avgHumidity = (openWeatherDTO.getMain().getHumidity() + weatherStackDTO.getCurrent().getHumidity()) / 2;
        final double avgPressure = (openWeatherDTO.getMain().getPressure() + weatherStackDTO.getCurrent().getPressure()) / 2;
        final double avgWindSpeed = (openWeatherDTO.getWind().getSpeed() + weatherStackDTO.getCurrent().getWind_speed()) / 2;
        final double avgWindDegree = (openWeatherDTO.getWind().getDeg() + weatherStackDTO.getCurrent().getWind_degree()) / 2;

        return Weather.builder()
                .cityName(openWeatherDTO.getName())
                .temperature(avgTemp)
                .humidity(avgHumidity)
                .pressure(avgPressure)
                .windSpeed(avgWindSpeed)
                .windDegree(avgWindDegree)
                .dateTime(LocalDateTime.now())
                .build();
    }
}
