package com.weather.weatherapp.service;

import com.weather.weatherapp.client.WeatherClient;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.dto.forecast.Forecast;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.repository.WeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private static final Forecast.ForecastDay FORECAST_DAY = Forecast.ForecastDay.builder()
            .forecastDate(LocalDate.now().plusDays(1))
            .temperature(27.26)
            .pressure(1020D)
            .humidity(62D)
            .windSpeed(7.05)
            .windDegree(101D)
            .build();
    private static final Forecast LOCATION_FORECAST = Forecast.builder()
            .cityName(CITY_NAME)
            .timezone("America/New_York")
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

    @Test
    void thatGetAllLocationsWeathersWorksCorrectly(){
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(LOCATION_WEATHER);
        when(weatherRepository.findAll()).thenReturn(weatherList);

        final List<Weather> actual = service.getAllLocationsWeathers();

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getCityName()).isEqualTo("Miami");
        assertThat(actual.get(0).getTemperature()).isEqualTo(27.4);
        assertThat(actual.get(0).getWindDegree()).isEqualTo(250D);
    }

    @Test
    void thatGetWeatherForecastWorksCorrectly() throws InterruptedException, IOException, NoLocationFoundException, URISyntaxException, InvalidDataException {
        List<Forecast.ForecastDay> forecastDayList = new ArrayList<>();
        forecastDayList.add(FORECAST_DAY);
        forecastDayList.add(FORECAST_DAY);
        LOCATION_FORECAST.setForecastDaysList(forecastDayList);

        when(locationService.findLocationById(anyLong())).thenReturn(LOCATION);
        when(weatherClient.getForecastForGeographicCoordinates(anyDouble(), anyDouble())).thenReturn(LOCATION_FORECAST);

        final Forecast actual = service.getWeatherForecast(1L, LocalDate.now().plusDays(1).toString());

        assertThat(actual).hasNoNullFieldsOrProperties();
        assertThat(actual.getCityName()).isEqualTo(CITY_NAME);
        assertThat(actual.getForecastDaysList()).hasSize(2);
        assertThat(actual.getForecastDaysList().get(0).getTemperature()).isEqualTo(27.26);
    }

    @Test
    void thatGetWeatherForecastThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(locationService.findLocationById(anyLong())).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.getWeatherForecast(999L, LocalDate.now().plusDays(1).toString()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @Test
    void thatGetWeatherForecastThrowsIOException() throws NoLocationFoundException, InterruptedException, IOException, URISyntaxException {
        when(locationService.findLocationById(anyLong())).thenReturn(LOCATION);
        when(weatherClient.getForecastForGeographicCoordinates(anyDouble(), anyDouble())).thenThrow(IOException.class);

        Throwable throwable = Assertions.assertThrows(IOException.class, () -> service.getWeatherForecast(999L, LocalDate.now().plusDays(1).toString()));

        assertThat(throwable).isExactlyInstanceOf(IOException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"sdjfksdfs", "2021-02-03", "2021/02/16", "2021-02-28", "2020:01:12"})
    void thatGetWeatherForecastThrowsInvalidDataException(String value) throws NoLocationFoundException, InterruptedException, IOException, URISyntaxException {
        List<Forecast.ForecastDay> forecastDayList = new ArrayList<>();
        forecastDayList.add(FORECAST_DAY);
        LOCATION_FORECAST.setForecastDaysList(forecastDayList);
        when(locationService.findLocationById(anyLong())).thenReturn(LOCATION);
        when(weatherClient.getForecastForGeographicCoordinates(anyDouble(), anyDouble())).thenReturn(LOCATION_FORECAST);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.getWeatherForecast(IDENTIFIER, value));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Wrong data provided");
    }
}
