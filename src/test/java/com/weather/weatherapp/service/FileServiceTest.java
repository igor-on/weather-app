package com.weather.weatherapp.service;

import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.exception.InvalidDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    private static final String LOCATIONS_FILE = "TestFile";
    private static final String LOCATIONS_WEATHERS_FILE = "TestFile2";
    private static final Location LOCATION = new Location(2L, "Miami", 25.7743, -80.1937, "US");
    private static final Weather LOCATION_WEATHER = Weather.builder()
            .id(1L)
            .cityName("Miami")
            .temperature(27.4)
            .humidity(54D)
            .pressure(1019D)
            .windSpeed(6.17)
            .windDegree(250D)
            .dateTime(LocalDateTime.now())
            .build();

    @Mock
    private LocationService locationService;
    @Mock
    private WeatherService weatherService;
    private FileService service;

    @BeforeEach
    void setUp() {
        service = new FileService(locationService, weatherService);
    }

    @Test
    void thatWriteLocationsToFileWorksCorrectly() throws IOException, InvalidDataException {
        List<Location> locationList = new ArrayList<>();
        locationList.add(LOCATION);
        when(locationService.getAllLocations()).thenReturn(locationList);

        final String actual = service.writeLocationsToFile(LOCATIONS_FILE);

        assertThat(actual).isEqualTo("Successfully wrote to file " + LOCATIONS_FILE + ".json");
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "", "12345678901234567890123456789012345678901"})
    void thatWriteLocationsToFileThrowsInvalidDataException(String value) {
        List<Location> locationList = new ArrayList<>();
        locationList.add(LOCATION);
        when(locationService.getAllLocations()).thenReturn(locationList);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.writeLocationsToFile(value));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given file name is incorrect");
    }

    @Test
    void thatWriteLocationsToFileThrowsInvalidDataExceptionOnNull() {
        List<Location> locationList = new ArrayList<>();
        locationList.add(LOCATION);
        when(locationService.getAllLocations()).thenReturn(locationList);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.writeLocationsToFile(null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given file name is incorrect");
    }

    @Test
    void thatWriteLocationsWeathersToFileWorksCorrectly() throws IOException, InvalidDataException {
        List<Weather> locationList = new ArrayList<>();
        locationList.add(LOCATION_WEATHER);
        when(weatherService.getAllLocationsWeathers()).thenReturn(locationList);

        final String actual = service.writeLocationsWeathersToFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actual).isEqualTo("Successfully wrote to file " + LOCATIONS_WEATHERS_FILE + ".json");
    }

    @ParameterizedTest
    @ValueSource(strings = {"   ", "", "12345678901234567890123456789012345678901"})
    void thatWriteLocationsWeathersToFileThrowsInvalidDataException(String value) {
        List<Weather> locationList = new ArrayList<>();
        locationList.add(LOCATION_WEATHER);
        when(weatherService.getAllLocationsWeathers()).thenReturn(locationList);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.writeLocationsWeathersToFile(value));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given file name is incorrect");
    }

    @Test
    void thatWriteLocationsWeathersToFileThrowsInvalidDataExceptionOnNull() {
        List<Weather> locationList = new ArrayList<>();
        locationList.add(LOCATION_WEATHER);
        when(weatherService.getAllLocationsWeathers()).thenReturn(locationList);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.writeLocationsWeathersToFile(null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given file name is incorrect");
    }
}