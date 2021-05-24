package com.weather.weatherapp.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.controller.WeatherController;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UserInterface {


    private final LocationController locationController;
    private final WeatherController weatherController;
    private final Scanner scanner = new Scanner(System.in);

    public void runApplication() {
        while (true) {
            System.out.println("Welcome in Weather APP, what do you want to do?");
            System.out.println("1. Add new localization");
            System.out.println("2. Remove localization");
            System.out.println("3. Find localization data by city name");
            System.out.println("4. Show all saved localizations");
            System.out.println("5. Save current weather for available localizations");
            System.out.println("6. Show all saved weathers");
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
                case 4:
                    showAllSavedLocations();
                    break;
                case 5:
                    saveLocationCurrentWeather();
                    break;
                case 6:
                    showAllSavedWeathers();
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
        showAllSavedLocations();
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

    private void showAllSavedLocations() {
        try {
            System.out.println(locationController.showAllSavedLocations());
        } catch (JsonProcessingException e) {
            System.out.println("Json o co ci chodzi");
        }
    }

    private void saveLocationCurrentWeather() {
        showAllSavedLocations();
        System.out.println("You want to add new location weather by its name(1) or geographical coords(2)?");
        int userChoice = scanner.nextInt();
        try {
            switch (userChoice) {
                case 1:
                    System.out.println("Choose location by its city name");
                    scanner.nextLine();
                    String selectedCity = scanner.nextLine();
                    System.out.println(weatherController.saveLocationWeatherByCityName(selectedCity));
                    break;
                case 2:
                    //You don't want to choose your location by its coords
                    System.out.println("Choose location by its geographical coordinates id");
                    long selectedId = scanner.nextLong();
                    System.out.println(weatherController.saveLocationWeatherByCoordinates(selectedId));
                    break;
                default:
                    System.out.println("There's no option like this!");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }
    }

    private void showAllSavedWeathers() {
        try {
            System.out.println(weatherController.showAllSavedWeathers());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("JSON HOW DARE YOU");
        }
    }
}
