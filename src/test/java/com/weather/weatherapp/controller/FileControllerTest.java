package com.weather.weatherapp.controller;

import com.weather.weatherapp.exception.InvalidDataException;
import com.weather.weatherapp.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {
    private static final String LOCATIONS_FILE = "Test File";
    private static final String LOCATIONS_WEATHERS_FILE = "Test File2";

    @Mock
    private FileService fileService;
    private FileController controller;

    @BeforeEach
    private void setUp() {
        controller = new FileController(fileService);
    }

    @Test
    void thatWriteLocationsToFileWorksCorrectly() throws IOException, InvalidDataException {
        when(fileService.writeLocationsToFile(LOCATIONS_FILE)).thenReturn("Successfully write to file " + LOCATIONS_FILE + ".json");

        final String actualJson = controller.writeLocationsToFile(LOCATIONS_FILE);

        assertThat(actualJson).isEqualTo("Successfully write to file " + LOCATIONS_FILE + ".json");
    }

    @Test
    void thatWriteLocationsToFileThrowsIOException() throws IOException, InvalidDataException {
        when(fileService.writeLocationsToFile(LOCATIONS_FILE)).thenThrow(IOException.class);

        final String actualJson = controller.writeLocationsToFile(LOCATIONS_FILE);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatWriteLocationsToFileThrowsInvalidDataException() throws IOException, InvalidDataException {
        when(fileService.writeLocationsToFile(anyString())).thenThrow(InvalidDataException.class);

        final String actualJson = controller.writeLocationsToFile(anyString());

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatWriteWeathersToFileWorksCorrectly() throws IOException, InvalidDataException {
        when(fileService.writeWeathersToFile(LOCATIONS_WEATHERS_FILE)).thenReturn("Successfully write to file " + LOCATIONS_WEATHERS_FILE + ".json");

        final String actualJson = controller.writeWeathersToFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actualJson).isEqualTo("Successfully write to file " + LOCATIONS_WEATHERS_FILE + ".json");
    }

    @Test
    void thatWriteWeathersToFileThrowsIOException() throws IOException, InvalidDataException {
        when(fileService.writeWeathersToFile(LOCATIONS_WEATHERS_FILE)).thenThrow(IOException.class);

        final String actualJson = controller.writeWeathersToFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatWriteWeathersToFileThrowsInvalidDataException() throws IOException, InvalidDataException {
        when(fileService.writeWeathersToFile(LOCATIONS_WEATHERS_FILE)).thenThrow(InvalidDataException.class);

        final String actualJson = controller.writeWeathersToFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatGetLocationsDataFromFileWorksCorrectly() throws IOException, InvalidDataException {
        when(fileService.getLocationsFromFile(LOCATIONS_FILE)).thenReturn("Successfully read from file " + LOCATIONS_FILE + ".json and saved in data base");

        final String actual = controller.getLocationsDataFromFile(LOCATIONS_FILE);

        assertThat(actual).isEqualTo("Successfully read from file " + LOCATIONS_FILE + ".json and saved in data base");
    }

    @Test
    void thatGetLocationsDataFromFileThrowsIOException() throws IOException, InvalidDataException {
        when(fileService.getLocationsFromFile(LOCATIONS_WEATHERS_FILE)).thenThrow(IOException.class);

        final String actualJson = controller.getLocationsDataFromFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actualJson).contains("\"message\" : \"Given objects from file can't be deserialized, wrong DTO\"");
    }

    @Test
    void thatGetLocationsDataFromFileThrowsInvalidDataException() throws IOException, InvalidDataException {
        when(fileService.getLocationsFromFile(anyString())).thenThrow(InvalidDataException.class);

        final String actualJson = controller.getLocationsDataFromFile(LOCATIONS_FILE);

        assertThat(actualJson).contains("\"message\" : ");
    }

    @Test
    void thatGetWeathersDataFromFileWorksCorrectly() throws IOException, InvalidDataException {
        when(fileService.getWeathersFromFile(LOCATIONS_WEATHERS_FILE)).thenReturn("Successfully read from file " + LOCATIONS_WEATHERS_FILE + ".json and saved in data base");

        final String actual = controller.getWeathersDataFromFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actual).isEqualTo("Successfully read from file " + LOCATIONS_WEATHERS_FILE + ".json and saved in data base");
    }

    @Test
    void thatGetWeathersDataFromFileThrowsIOException() throws IOException, InvalidDataException {
        when(fileService.getWeathersFromFile(LOCATIONS_FILE)).thenThrow(IOException.class);

        final String actualJson = controller.getWeathersDataFromFile(LOCATIONS_FILE);

        assertThat(actualJson).contains("\"message\" : \"Given objects from file can't be deserialized, wrong DTO\"");
    }

    @Test
    void thatGetWeathersDataFromFileThrowsInvalidDataException() throws IOException, InvalidDataException {
        when(fileService.getWeathersFromFile(LOCATIONS_WEATHERS_FILE)).thenThrow(InvalidDataException.class);

        final String actualJson = controller.getWeathersDataFromFile(LOCATIONS_WEATHERS_FILE);

        assertThat(actualJson).contains("\"message\" : ");
    }
}