package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.weatherapp.dto.StatisticalWeatherDTO;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.current.WeatherDTO;
import com.weather.weatherapp.dto.forecast.Forecast;
import com.weather.weatherapp.dto.forecast.ForecastDTO;
import com.weather.weatherapp.mapper.WeatherDTOMapper;
import com.weather.weatherapp.service.WeatherService;
import com.weather.weatherapp.exception.Error;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.weather.weatherapp.mapper.ForecastDTOMapper.mapToForecastDTO;
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

    public String saveLocationWeatherByCoordinates(long id) throws JsonProcessingException {
        try {
            final Weather createdByCoordinatesLocationWeather = weatherService.getAndSaveWeatherByGeographicCoordinates(id);

            final WeatherDTO locationWeatherDTO = mapToWeatherDTO(createdByCoordinatesLocationWeather);

            System.out.println("little validation(or not) if your geographical coordinates were strange.. :)");
            return mapper.writeValueAsString(locationWeatherDTO);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String showAllSavedWeathers() throws JsonProcessingException {
        try {
            final List<Weather> all = weatherService.getAllLocationsWeathers();

            final List<WeatherDTO> mappedAll = all.stream()
                    .map(WeatherDTOMapper::mapToWeatherDTO)
                    .collect(Collectors.toList());

            return mapper.writeValueAsString(mappedAll);
        } catch (JsonProcessingException e) {
            return mapper.writeValueAsString(new Error("Something went wrong :/"));
        }
    }

    public String getLocationForecast(Long id, String selectedDate) throws JsonProcessingException {
        try {
            final Forecast weatherForecast = weatherService.getWeatherForecast(id, selectedDate);

            final ForecastDTO weatherForecastDTO = mapToForecastDTO(weatherForecast);

            return mapper.writeValueAsString(weatherForecastDTO);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String getLocationWeatherStatisticalData(String cityName) throws JsonProcessingException {
        try {
            final StatisticalWeatherDTO statisticalData = weatherService.getStatisticalData(cityName);

            return mapper.writeValueAsString(statisticalData);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
