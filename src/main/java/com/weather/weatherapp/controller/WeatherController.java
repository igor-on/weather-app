package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.WeatherDTO;
import com.weather.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;

import static com.weather.weatherapp.mapper.WeatherDTOMapper.mapToWeatherDTO;

@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    public String saveLocationWeatherByCityName(String cityName) throws JsonProcessingException {
        try {
            final Weather createdByCityNameLocationWeather = weatherService.getAndSaveWeatherByCityName(cityName);

            final WeatherDTO locationWeatherDTO = mapToWeatherDTO(createdByCityNameLocationWeather);

            return mapper.writeValueAsString(locationWeatherDTO);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
