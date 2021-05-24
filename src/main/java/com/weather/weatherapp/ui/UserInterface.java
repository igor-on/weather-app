package com.weather.weatherapp.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weatherapp.controller.LocationController;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UserInterface {

    private final LocationController locationController;
    private final Scanner scanner = new Scanner(System.in);

    public void runApplication() {
        while (true) {
            System.out.println("Welcome in Weather APP, what do you want to do?");
            System.out.println("1. Add new localization");
            System.out.println("2. Remove localization");
            System.out.println("3. Find localization data by city name");
            System.out.println("0. Close app");

            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1:
                    addLocation();
                    break;
                case 2:
                    removeLocation();
                    break;
                case 3:
                    findLocationByCityName();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("There's no options like this!");
            }
        }
    }

    private void addLocation() {
        System.out.println("Write weather location name");
        //String scanner problem
        scanner.nextLine();
        String cityName = scanner.nextLine();
        System.out.println("Now write latitude");
        double latitude = scanner.nextDouble();
        System.out.println("Now write longitude");
        double longitude = scanner.nextDouble();
        System.out.println("Now write region");
        scanner.nextLine();
        String region = scanner.nextLine();
        System.out.println("Now write country");
        String country = scanner.nextLine();
        try {
            System.out.println(locationController.addLocation(cityName, latitude, longitude, region, country));
        } catch (JsonProcessingException e) {
            System.out.println("JSON STOP PLEASE");
        }
    }

    private void removeLocation() {
        System.out.println("Choose location to remove by it's id");
        long selectedId = scanner.nextLong();
        try {
            System.out.println(locationController.removeLocation(selectedId));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findLocationByCityName() {
        System.out.println("Write localization city name you want to find");
        scanner.nextLine();
        String selectedCity = scanner.nextLine();
        try {
            System.out.println(locationController.findLocationByCityName(selectedCity));
        } catch (JsonProcessingException e) {
            System.out.println("JSON CO JEST");
        }
    }
}
