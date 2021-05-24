package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.weatherapp.service.FileService;
import com.weather.weatherapp.exception.Error;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public String writeLocationsToFile(String fileName) throws JsonProcessingException {
        try {
            return fileService.writeLocationsToFile(fileName);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String writeWeathersToFile(String fileName) throws JsonProcessingException {
        try {
            return fileService.writeWeathersToFile(fileName);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String getLocationsDataFromFile(String fileName) throws JsonProcessingException {
        try {
            return fileService.getLocationsFromFile(fileName);
        } catch (IOException e) {
            return mapper.writeValueAsString(new Error("Given objects from file can't be deserialized, wrong DTO"));
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }

    public String getWeathersDataFromFile(String fileName) throws JsonProcessingException {
        try {
            return fileService.getWeathersFromFile(fileName);
        } catch (IOException e) {
            return mapper.writeValueAsString(new Error("Given objects from file can't be deserialized, wrong DTO"));
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
