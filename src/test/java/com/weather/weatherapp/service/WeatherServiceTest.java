package com.weather.weatherapp.service;

import com.weather.weatherapp.client.WeatherClient;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.repository.WeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    private static final Long IDENTIFIER = 1L;
    private static final String CITY_NAME = "Miami";
    private static final Location LOCATION = new Location(IDENTIFIER, CITY_NAME, 25.7743, -80.1937, "null", "US");
    private static final Weather LOCATION_WEATHER = Weather.builder()
            .id(IDENTIFIER)
            .cityName(CITY_NAME)
            .temperature(27.4)
            .humidity(54D)
            .pressure(1019D)
            .windSpeed(6.17)
            .windDegree(250D)
            .dateTime(LocalDateTime.now())
            .build();

    @Mock
    private WeatherClient weatherClient;
    @Mock
    private LocationService locationService;
    @Mock
    private WeatherRepository weatherRepository;

    private WeatherService service;

    @BeforeEach
    void setUp() {
        service = new WeatherService(weatherClient, locationService, weatherRepository);
    }

    @Test
    void thatSaveLocationWeatherByCityNameWorkCorrectly() throws InterruptedException, URISyntaxException, IOException, InvalidDataException, NoLocationFoundException {
        when(locationService.findLocation(any())).thenReturn(LOCATION);
        when(weatherClient.getWeatherForCityName(anyString())).thenReturn(LOCATION_WEATHER);
        when(weatherRepository.save(any())).thenReturn(LOCATION_WEATHER);

        final Weather actual = service.getAndSaveWeatherByCityName("Miami");

        assertThat(actual.getCityName()).isEqualTo("Miami");
        assertThat(actual.getTemperature()).isEqualTo(27.4);
        assertThat(actual.getWindDegree()).isEqualTo(250D);
        assertThat(actual).hasNoNullFieldsOrProperties();
        assertThat(actual).hasSameHashCodeAs(LOCATION_WEATHER);
        verify(locationService, times(1)).findLocation(anyString());
        verify(weatherClient, times(1)).getWeatherForCityName(anyString());
        verify(weatherRepository, times(1)).save(any());
    }

    @Test
    void thatSaveLocationWeatherByCityNameThrowsNoLocationFoundException() throws NoLocationFoundException, InvalidDataException {
        when(locationService.findLocation(anyString())).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.getAndSaveWeatherByCityName("Miami"));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @Test
    void thatSaveLocationWeatherByCityNameThrowsIOException() throws NoLocationFoundException, InterruptedException, IOException, URISyntaxException, InvalidDataException {
        when(locationService.findLocation(CITY_NAME)).thenReturn(LOCATION);
        when(weatherClient.getWeatherForCityName(CITY_NAME)).thenThrow(IOException.class);

        Throwable throwable = Assertions.assertThrows(IOException.class, () -> service.getAndSaveWeatherByCityName(CITY_NAME));

        assertThat(throwable).isExactlyInstanceOf(IOException.class);
    }

    @Test
    void thatSaveLocationWeatherByCoordsWorksCorrectly() throws InterruptedException, URISyntaxException, NoLocationFoundException, IOException {
        when(locationService.findLocationById(anyLong())).thenReturn(LOCATION);
        when(weatherClient.getWeatherForGeographicCoordinates(anyDouble(), anyDouble())).thenReturn(LOCATION_WEATHER);
        when(weatherRepository.save(LOCATION_WEATHER)).thenReturn(LOCATION_WEATHER);

        final Weather actual = service.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER);

        assertThat(actual.getCityName())
                .isNotNull()
                .isEqualTo(LOCATION_WEATHER.getCityName());
        assertThat(actual).hasNoNullFieldsOrProperties();
        assertThat(actual).hasSameHashCodeAs(LOCATION_WEATHER);
        verify(locationService, times(1)).findLocationById(anyLong());
        verify(weatherClient, times(1)).getWeatherForGeographicCoordinates(anyDouble(), anyDouble());
        verify(weatherRepository, times(1)).save(any());
    }

    @Test
    void thatSaveLocationByCoordsThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(locationService.findLocationById(IDENTIFIER)).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.getAndSaveWeatherByGeographicCoordinates(IDENTIFIER));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @Test
    void thatSaveLocationByCoordsThrowsIOException() throws InterruptedException, URISyntaxException, NoLocationFoundException, IOException {
        when(locationService.findLocationById(anyLong())).thenReturn(LOCATION);
        when(weatherClient.getWeatherForGeographicCoordinates(anyDouble(), anyDouble())).thenThrow(IOException.class);

        Throwable throwable = Assertions.assertThrows(IOException.class, () -> service.getAndSaveWeatherByGeographicCoordinates(anyLong()));

        assertThat(throwable).isExactlyInstanceOf(IOException.class);
    }
}