package com.weather.weatherapp.mapper;

import com.weather.weatherapp.dto.forecast.Forecast;
import com.weather.weatherapp.dto.forecast.ForecastDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ForecastDTOMapper {

    public static ForecastDTO mapToForecastDTO(Forecast weatherForecast) {
        if(weatherForecast.getForecastDaysList().size() > 1){
            throw new IllegalArgumentException("There can't be more than one result");
        }

        final Forecast.ForecastDay oneAndOnlySelectedForecast = weatherForecast.getForecastDaysList().get(0);

        final LocalDate forecastDate = oneAndOnlySelectedForecast.getForecastDate();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, E");
        final String stringDate = forecastDate.format(formatter);

        return ForecastDTO.builder()
                .cityName(weatherForecast.getCityName())
                .timeZone(weatherForecast.getTimezone())
                .forecastDate(stringDate)
                .temperature(oneAndOnlySelectedForecast.getTemperature())
                .humidity(oneAndOnlySelectedForecast.getHumidity())
                .pressure(oneAndOnlySelectedForecast.getPressure())
                .windSpeed(oneAndOnlySelectedForecast.getWindSpeed())
                .windDegree(oneAndOnlySelectedForecast.getWindDegree())
                .build();
    }
}
