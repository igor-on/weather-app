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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void thatGetAllLocationsWorksCorrectly() {
        List<Location> ALLLOCATIONS = new ArrayList<>();
        ALLLOCATIONS.add(LOCATION);
        when(locationRepository.findAll()).thenReturn(ALLLOCATIONS);

        final List<Location> actual = service.getAllLocations();

        assertThat(actual).contains(LOCATION);
        assertThat(actual).hasSize(1);
        assertThat(actual).hasOnlyElementsOfType(Location.class);
    }

    @Test
    void thatUpdateLocationCityNameWorksCorrectly() throws NoLocationFoundException, InvalidDataException {
        String testCityName = "Majami";
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);
        LOCATION.setCityName(testCityName);
        when(locationRepository.update(any())).thenReturn(LOCATION);

        final Location actual = service.updateLocationCityName(IDENTIFIER, testCityName);

        assertThat(actual.getCityName())
                .isEqualTo(testCityName);
        assertThat(actual.getCityName())
                .isNotEqualTo(CITY_NAME);

        assertThat(actual.getId())
                .isNotNull()
                .isEqualTo(IDENTIFIER);

        assertThat(actual.getLatitude())
                .isEqualTo(LATITUDE);

        assertThat(actual.getLongitude())
                .isEqualTo(LONGITUDE);

        assertThat(actual.getRegion())
                .isEqualTo(REGION);

        assertThat(actual.getCountry())
                .isEqualTo(COUNTRY);

        verify(locationRepository, times(1)).find(anyLong());
        verify(locationRepository, times(1)).update(any());
    }

    @Test
    void thatUpdateCityNameThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenThrow(NoLocationFoundException.class);

        //na obiekcie który nie jest Mockiem wystarczy uzupelnic tylko jeden(lub większość np 2 an 3 (?)) argument czyms konkretnym?
        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.updateLocationCityName(IDENTIFIER, anyString()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    void thatUpdateCityNameThrowsInvalidDataException(String value) throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.updateLocationCityName(anyLong(), value));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("City name can't be empty");
    }

    @Test
    void thatUpdateCityNameThrowsInvalidDataExceptionOnNull() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.updateLocationCityName(anyLong(), null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("City name can't be empty");
    }

    @Test
    void thatUpdateLocationCoordsWorksCorrectly() throws NoLocationFoundException, InvalidDataException {
        //given
        double testLat = 12.345;
        double testLon = 89.678;
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);
        LOCATION.setLatitude(testLat);
        LOCATION.setLongitude(testLon);
        when(locationRepository.update(any())).thenReturn(LOCATION);

        //when
        final Location actual = service.updateLocationCoords(IDENTIFIER, testLat, testLon);

        //then
        assertThat(actual.getCityName())
                .isEqualTo(CITY_NAME);

        assertThat(actual.getId())
                .isNotNull()
                .isEqualTo(IDENTIFIER);

        assertThat(actual.getLatitude())
                .isEqualTo(testLat);

        assertThat(actual.getLongitude())
                .isEqualTo(testLon);

        assertThat(actual.getLatitude())
                .isNotEqualTo(LATITUDE);

        assertThat(actual.getLongitude())
                .isNotEqualTo(LONGITUDE);

        assertThat(actual.getRegion())
                .isEqualTo(REGION);

        assertThat(actual.getCountry())
                .isEqualTo(COUNTRY);

        verify(locationRepository, times(1)).find(anyLong());
        verify(locationRepository, times(1)).update(any());
    }

    @Test
    void thatUpdateCoordsThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.updateLocationCoords(IDENTIFIER, LATITUDE, anyDouble()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"91, 155", "56.9811, -183.091", "-94.234, 120.32", "-91, -184", "91, 184"})
    void thatUpdateCoordsThrowsInvalidDataException(double lat, double lon) throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class, () ->
                service.updateLocationCoords(anyLong(), lat, lon));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class)
                .hasMessage("Given geographical coordinates are incorrect");
    }

    @Test
    void thatUpdateLocationRegionWorksCorrectly() throws NoLocationFoundException, InvalidDataException {
        //given
        String testRegion = "Zasiedmiogórogród";
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);
        LOCATION.setRegion(testRegion);
        when(locationRepository.update(any())).thenReturn(LOCATION);

        //when
        final Location actual = service.updateLocationRegion(IDENTIFIER, testRegion);

        //then
        assertThat(actual.getRegion()).isEqualTo(testRegion);
        assertThat(actual.getRegion()).isNotEqualTo(REGION);

        assertThat(actual.getCityName())
                .isEqualTo(CITY_NAME);

        assertThat(actual.getId())
                .isNotNull()
                .isEqualTo(IDENTIFIER);

        assertThat(actual.getLatitude())
                .isEqualTo(LATITUDE);

        assertThat(actual.getLongitude())
                .isEqualTo(LONGITUDE);

        assertThat(actual.getCountry())
                .isEqualTo(COUNTRY);

        verify(locationRepository, times(1)).find(anyLong());
        verify(locationRepository, times(1)).update(any());
    }

    @Test
    void thatUpdateRegionThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.updateLocationRegion(IDENTIFIER, anyString()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    void thatUpdateRegionThrowsInvalidDataException(String value) throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.updateLocationRegion(anyLong(), value));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given region is incorrect");
    }

    @Test
    void thatUpdateRegionThrowsInvalidDataExceptionOnNull() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.updateLocationRegion(anyLong(), null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given region is incorrect");
    }

    @Test
    void thatUpdateLocationCountryWorksCorrectly() throws NoLocationFoundException, InvalidDataException {
        //given
        String testCountry = "UNITED STATES";
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);
        LOCATION.setCountry(testCountry);
        when(locationRepository.update(any())).thenReturn(LOCATION);

        //when
        final Location actual = service.updateLocationCountry(IDENTIFIER, testCountry);

        //then
        assertThat(actual.getCountry()).isEqualTo(testCountry);
        assertThat(actual.getCountry()).isNotEqualTo(COUNTRY);


        assertThat(actual.getCityName())
                .isEqualTo(CITY_NAME);

        assertThat(actual.getId())
                .isNotNull()
                .isEqualTo(IDENTIFIER);

        assertThat(actual.getLatitude())
                .isEqualTo(LATITUDE);

        assertThat(actual.getLongitude())
                .isEqualTo(LONGITUDE);

        assertThat(actual.getRegion())
                .isEqualTo(REGION);

        verify(locationRepository, times(1)).find(anyLong());
        verify(locationRepository, times(1)).update(any());
    }

    @Test
    void thatUpdateCountryThrowsNoLocationFoundException() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenThrow(NoLocationFoundException.class);

        Throwable throwable = Assertions.assertThrows(NoLocationFoundException.class, () -> service.updateLocationCountry(IDENTIFIER, anyString()));

        assertThat(throwable).isExactlyInstanceOf(NoLocationFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    void thatUpdateCountryThrowsInvalidDataException(String value) throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.updateLocationCountry(anyLong(), value));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given country is incorrect");
    }

    @Test
    void thatUpdateCountryThrowsInvalidDataExceptionOnNull() throws NoLocationFoundException {
        when(service.findLocationById(anyLong())).thenReturn(LOCATION);

        Throwable throwable = Assertions.assertThrows(InvalidDataException.class,
                () -> service.updateLocationCountry(anyLong(), null));

        assertThat(throwable).isExactlyInstanceOf(InvalidDataException.class).hasMessage("Given country is incorrect");
    }
}