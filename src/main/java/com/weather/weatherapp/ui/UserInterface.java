package com.weather.weatherapp.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weatherapp.controller.FileController;
import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.controller.WeatherController;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UserInterface {


    private final LocationController locationController;
    private final WeatherController weatherController;
    private final FileController fileController;
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
            System.out.println("7. Update localization");
            System.out.println("8. Get weather forecast for available localizations");
            System.out.println("9. Get statistical weather from last month");
            System.out.println("10. Write to Json file");
            System.out.println("11. Get data from Json file");
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
                case 7:
                    updateLocation();
                    break;
                case 8:
                    getLocationForecast();
                    break;
                case 9:
                    getStatisticalWeather();
                    break;
                case 10:
                    writeToFile();
                    break;
                case 11:
                    getDataFromFile();
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

    private void updateLocation() {
        System.out.println("What are you want to update?");
        System.out.println("1. City name");
        System.out.println("2. Geographical Coordination");
        System.out.println("3. Region");
        System.out.println("4. Country");
        int userChoice = scanner.nextInt();
        showAllSavedLocations();
        System.out.println("Choose location to update by its id");
        long selectedId = scanner.nextLong();
        try {
            switch (userChoice) {
                case 1:
                    System.out.println("Write new city name for your location");
                    scanner.nextLine();
                    String cityName = scanner.nextLine();
                    System.out.println(locationController.updateLocationCityName(selectedId, cityName));
                    break;
                case 2:
                    System.out.println("Write new coordination for your location");
                    System.out.println("Write latitude");
                    double latitude = scanner.nextDouble();
                    System.out.println("Write longitude");
                    double longitude = scanner.nextDouble();
                    System.out.println(locationController.updateLocationCoords(selectedId, latitude, longitude));
                    break;

                case 3:
                    System.out.println("Write new region for your location");
                    scanner.nextLine();
                    final String region = scanner.nextLine();
                    System.out.println(locationController.updateLocationRegion(selectedId, region));
                    break;
                case 4:
                    System.out.println("Write new country for your location");
                    scanner.nextLine();
                    final String country = scanner.nextLine();
                    System.out.println(locationController.updateLocationCountry(selectedId, country));
                    break;
                default:
                    System.out.println("There's no options like this!");
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getLocationForecast() {
        showAllSavedLocations();
        System.out.println("Write location id for which you want to check weather forecast");
        final Long selectedId = scanner.nextLong();
        System.out.println("Select date for the forecast check(max 7 days +)(optional)");
        System.out.println("Required format: yyyy-MM-dd");
        final String selectedDate = scanner.nextLine();
        try {
            System.out.println(weatherController.getLocationForecast(selectedId, selectedDate));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("JSON NOOOO");
        }
    }

    private void getStatisticalWeather() {
        showAllSavedWeathers();
        System.out.println("Write location city name for which you want to get statistical data");
        scanner.nextLine();
        final String selectedLocation = scanner.nextLine();
        try {
            System.out.println("Statistical data from last month");
            System.out.println(weatherController.getLocationWeatherStatisticalData(selectedLocation));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("JOSOOON");
        }
    }


    private void writeToFile() {
        System.out.println("What are you want write to file?");
        System.out.println("1) Saved locations");
        System.out.println("2) Saved locations weathers");
        final int userChoice = scanner.nextInt();
        System.out.println("Select file name");
        scanner.nextLine();
        final String fileName = scanner.nextLine();

        try {
            switch (userChoice) {
                case 1:
                    System.out.println(fileController.writeLocationsToFile(fileName));
                    break;
                case 2:
                    System.out.println(fileController.writeWeathersToFile(fileName));
                    break;
                default:
                    System.out.println("There's no option like this!");
            }
        } catch (JsonProcessingException e) {
            System.out.println("JSON STOP STOP");
        }
    }

    private void getDataFromFile() {
        System.out.println("What kind of data you want to read from file?");
        System.out.println("1) Saved locations");
        System.out.println("2) Saved locations weathers");
        final int userChoice = scanner.nextInt();
        System.out.println("Select file name");
        scanner.nextLine();
        String fileName = scanner.nextLine();

        try {
            switch (userChoice) {
                case 1:
                    System.out.println(fileController.getLocationsDataFromFile(fileName));
                    break;
                case 2:
                    System.out.println(fileController.getWeathersDataFromFile(fileName));
                    break;
                default:
                    System.out.println("There's no option like this!");
            }
        } catch (JsonProcessingException e) {
            System.out.println("JSON PLEASE STOP");
        }
    }
}
