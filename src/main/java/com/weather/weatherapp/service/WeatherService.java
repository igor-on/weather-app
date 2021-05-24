package com.weather.weatherapp.service;

import com.weather.weatherapp.client.WeatherClient;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.dto.StatisticalWeatherDTO;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.forecast.Forecast;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Weather getAndSaveWeatherByGeographicCoordinates(long id) throws NoLocationFoundException, InterruptedException, IOException, URISyntaxException {
        Location foundLocation = locationService.findLocationById(id);
        Weather foundLocationWeather
                = weatherClient.getWeatherForGeographicCoordinates(foundLocation.getLatitude(), foundLocation.getLongitude());

        return weatherRepository.save(foundLocationWeather);
    }

    public List<Weather> getAllLocationsWeathers() {
        return weatherRepository.findAll();
    }

    public Forecast getWeatherForecast(Long id, String selectedDate) throws NoLocationFoundException, URISyntaxException, IOException, InterruptedException, InvalidDataException {
        final Location foundLocation = locationService.findLocationById(id);

        final Forecast locationForecast
                = weatherClient.getForecastForGeographicCoordinates(foundLocation.getLatitude(), foundLocation.getLongitude());

        setUpForecastDate(selectedDate, foundLocation, locationForecast);

        return locationForecast;
    }

    private void setUpForecastDate(String selectedDate, Location foundLocation, Forecast locationForecast) throws InvalidDataException {
        locationForecast.setCityName(foundLocation.getCityName());

        if (selectedDate.isBlank()) {
            setUpDefaultDate(locationForecast);
        } else {
            setUpSelectedByUserDate(selectedDate, locationForecast);
        }
    }

    private void setUpDefaultDate(Forecast locationForecast) {
        LocalDate date = LocalDate.now().plusDays(1);
        final String defaultDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        final List<Forecast.ForecastDay> defaultForecast =
                filterForecastDays(defaultDate, locationForecast.getForecastDaysList());

        locationForecast.setForecastDaysList(defaultForecast);
    }

    private void setUpSelectedByUserDate(String selectedDate, Forecast locationForecast) throws InvalidDataException {
        final List<Forecast.ForecastDay> selectedByUserForecast =
                filterForecastDays(selectedDate, locationForecast.getForecastDaysList());

        if (selectedByUserForecast.isEmpty()) {
            throw new InvalidDataException("Wrong data provided");
        }

        locationForecast.setForecastDaysList(selectedByUserForecast);
    }

    private List<Forecast.ForecastDay> filterForecastDays(String stringDate, List<Forecast.ForecastDay> forecastDays) {
        return forecastDays.stream()
                .filter(singleDay -> singleDay.getForecastDate().toString().equals(stringDate))
                .collect(Collectors.toList());
    }

    public StatisticalWeatherDTO getStatisticalData(String cityName) throws InvalidDataException, NoLocationFoundException {
        final List<Object[]> results = weatherRepository.findStatisticalWeatherDataFromLastMonthByCityName(cityName);
        List<StatisticalWeatherDTO> list = new ArrayList<>();

        if(results.size() == 0){
            throw new NoLocationFoundException("There is no location with given city name: " + cityName);
        }
        if(results.size() > 1){
            throw new InvalidDataException("There can't be more than one result");
        }

        for (Object[] location : results) {
            final StatisticalWeatherDTO build = StatisticalWeatherDTO.builder()
                    .cityName((String) location[0])
                    .temperature((double) location[1])
                    .humidity((double) location[2])
                    .pressure((double) location[3])
                    .windSpeed((double) location[4])
                    .windDegree((double) location[5])
                    .build();
            list.add(build);
        }

        return list.get(0);
    }
}
