package com.weather.weatherapp.service;

import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.exception.NoLocationFoundException;
import com.weather.weatherapp.repository.LocationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    private static final Long IDENTIFIER = 2L;
    public static final String CITY_NAME = "Miami";
    public static final Double LATITUDE = 25.7743;
    public static final Double LONGITUDE = -80.1937;
    public static final String REGION = null;
    public static final String COUNTRY = "US";
    private static final Location LOCATION = new Location(IDENTIFIER, CITY_NAME, LATITUDE, LONGITUDE, COUNTRY);

    @Mock
    private LocationRepository locationRepository;
    private LocationService service;

    @BeforeEach
    void setUp() {
        service = new LocationService(locationRepository);
    }

    @AfterEach
    void set() {
        LOCATION.setId(IDENTIFIER);
        LOCATION.setCityName(CITY_NAME);
        LOCATION.setLatitude(LATITUDE);
        LOCATION.setLongitude(LONGITUDE);
        LOCATION.setRegion(REGION);
        LOCATION.setCountry(COUNTRY);
    }

    @Test
    void addLocationWorksCorrectly() throws InvalidDataException {
        //given
        when(locationRepository.save(any())).thenReturn(LOCATION);

        //when
        final Location actual = service.createAndSaveLocation(CITY_NAME, LATITUDE, LONGITUDE, "  ", COUNTRY);

        //then
        assertThat(actual.getCityName()).isEqualTo("Miami");
        assertThat(actual.getRegion()).isNull();
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    void thatAddLocationThrowsExceptionOnInvalidCityName() {
        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.createAndSaveLocation(null, LATITUDE, LONGITUDE, REGION, COUNTRY));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("City name can't be empty");
    }

    @Test
    void thatAddLocationThrowsExceptionOnBlankCityName() {
        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.createAndSaveLocation("  ", LATITUDE, LONGITUDE, REGION, COUNTRY));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("City name can't be empty");
    }

    @ParameterizedTest
    @CsvSource(value = {"91, 155", "56.9811, -183.091", "-94.234, 120.32", "-91, -184", "91, 184"})
    void thatAddLocationThrowsExceptionOnInvalidCoords(double lat, double lon) {
        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.createAndSaveLocation(CITY_NAME, lat, lon, REGION, COUNTRY));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given geographical coordinates aren't correct");
    }

    @Test
    void thatAddLocationThrowsExceptionOnInvalidCountry() {
        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.createAndSaveLocation(CITY_NAME, LATITUDE, LONGITUDE, REGION, null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given country is not correct");
    }

    @Test
    void thatAddLocationThrowsExceptionOnBlankCountry() {
        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.createAndSaveLocation(CITY_NAME, LATITUDE, LONGITUDE, REGION, "   "));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given country is not correct");
    }

    @Test
    void thatRemoveLocationWorksCorrectly() throws NoLocationFoundException {
        when(locationRepository.find(anyLong())).thenReturn(LOCATION);
        doNothing().when(locationRepository).remove(LOCATION);

        service.removeLocation(IDENTIFIER);

        verify(locationRepository, times(1)).remove(LOCATION);
        verify(locationRepository, times(1)).find(anyLong());
    }

    @Test
    void thatRemoveLocationThrowsNoLocationFoundException() throws NoLocationFoundException {
        doThrow(NoLocationFoundException.class).when(locationRepository).find(anyLong());

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.removeLocation(anyLong()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @Test
    void thatFindLocationByIdWorksCorrectly() throws NoLocationFoundException {
        when(locationRepository.find(anyLong())).thenReturn(LOCATION);

        final Location actual = service.findLocationById(anyLong());

        assertThat(actual.getCityName()).isEqualTo("Miami");
        verify(locationRepository, times(1)).find(anyLong());
    }

    @Test
    void thatFindLocationByIdThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(locationRepository.find(anyLong())).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.findLocationById(anyLong()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @Test
    void thatFindLocationByCityNameWorksCorrectly() throws NoLocationFoundException, InvalidDataException {
        when(locationRepository.findByName(CITY_NAME)).thenReturn(LOCATION);

        final Location actual = service.findLocation(CITY_NAME);

        assertThat(actual.getCityName()).isEqualTo("Miami");
        verify(locationRepository, times(1)).findByName(CITY_NAME);
    }

    @Test
    void thatFindLocationByCityNameThrowsInvalidDataExceptionOnNull() {

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.findLocation(null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given city name is not correct");
    }

    @Test
    void thatFindLocationByCityNameThrowsInvalidDataExceptionOnBlank() {

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () -> service.findLocation("   "));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given city name is not correct");
    }

    @Test
    void thatFindLocationByCityNameThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(locationRepository.findByName(CITY_NAME)).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.findLocation(CITY_NAME));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }
}