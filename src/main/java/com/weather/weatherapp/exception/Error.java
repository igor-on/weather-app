package com.weather.weatherapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Error {

    private String message;

    public Error(){
        message = "Something went wrong :/";
    }
}
