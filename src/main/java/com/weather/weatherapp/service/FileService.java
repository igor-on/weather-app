package com.weather.weatherapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.dto.Weather;
import com.weather.weatherapp.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FileService {

    private final LocationService locationService;
    private final WeatherService weatherService;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .setDateFormat(DateFormat.getDateTimeInstance())
            .configure(SerializationFeature.INDENT_OUTPUT, true);


    public String writeLocationsToFile(String fileName) throws IOException, InvalidDataException {
        final List<Location> allLocations = locationService.getAllLocations();
        final List<Location> allLocationsCopy = new ArrayList<>(allLocations);

        allLocationsCopy.forEach(e -> e.setId(null));

        final File file = getJsonFile(fileName);

        mapper.writeValue(file, allLocationsCopy);
        return getSuccessfulWriteMessage(file);
    }

    public String writeLocationsWeathersToFile(String fileName) throws IOException, InvalidDataException {
        final List<Weather> allLocationsWeathers = weatherService.getAllLocationsWeathers();
        List<Weather> allLocationsWeathersCopy = new ArrayList<>(allLocationsWeathers);

        allLocationsWeathersCopy.forEach(e -> e.setId(null));

        final File file = getJsonFile(fileName);

        mapper.writeValue(file, allLocationsWeathersCopy);
        return getSuccessfulWriteMessage(file);
    }

    private String getSuccessfulWriteMessage(File file) {
        return "Successfully wrote to file " + file.getName();
    }

    public String getLocationsFromFile(String fileName) throws IOException, InvalidDataException {
        final File file = getJsonFile(fileName);

        final Location[] locations = mapper.readValue(file, Location[].class);
        final List<Location> locationList = List.of(locations);

        locationService.saveLocationListInDB(locationList);

        return getSuccessfulReadMessage(file);
    }

    public String getLocationsWeathersFromFile(String fileName) throws IOException, InvalidDataException {
        final File file = getJsonFile(fileName);

        final Weather[] locationsWeathers = mapper.readValue(file, Weather[].class);
        final List<Weather> locationWeatherList = List.of(locationsWeathers);

        weatherService.saveWeatherListInDB(locationWeatherList);

        return getSuccessfulReadMessage(file);
    }

    private String getSuccessfulReadMessage(File file) {
        return "Successfully read from file " + file.getName() + " and saved in data base";
    }

    private File getJsonFile(String fileName) throws InvalidDataException {
        if(fileName == null || fileName.isBlank() || fileName.length() > 40){
            throw new InvalidDataException("Given file name is incorrect");
        }
        return new File(String.format("src/main/resources/%s.json", fileName));
    }
}
