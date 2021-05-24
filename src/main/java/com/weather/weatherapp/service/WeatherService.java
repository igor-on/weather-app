package com.weather.weatherapp.service;

import com.weather.weatherapp.client.WeatherClient;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;

@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;
    private final LocationService locationService;
    private final WeatherRepository weatherRepository;

    public Weather getAndSaveWeatherByCityName(String cityName) throws InterruptedException, IOException, URISyntaxException, NoLocationFoundException, InvalidDataException {
        Location foundLocation = locationService.findLocation(cityName);
        Weather foundLocationWeather
                = weatherClient.getWeatherForCityName(foundLocation.getCityName());

        return weatherRepository.save(foundLocationWeather);
    }
}
