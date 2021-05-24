package com.weather.weatherapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.weather.weatherapp.service.FileService;
import lombok.RequiredArgsConstructor;

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

    public String writeLocationsWeathersToFile(String fileName) throws JsonProcessingException {
        try {
            return fileService.writeLocationsWeathersToFile(fileName);
        } catch (Exception e) {
            return mapper.writeValueAsString(new Error(e.getMessage()));
        }
    }
}
