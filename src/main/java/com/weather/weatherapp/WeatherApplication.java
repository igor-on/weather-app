package com.weather.weatherapp;

import com.weather.weatherapp.client.WeatherClient;
import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.controller.WeatherController;
import com.weather.weatherapp.repository.LocationRepository;
import com.weather.weatherapp.repository.WeatherRepository;
import com.weather.weatherapp.service.LocationService;
import com.weather.weatherapp.service.WeatherService;
import com.weather.weatherapp.ui.UserInterface;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class WeatherApplication {
    public static void main(String[] args) {
         StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

         SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        final WeatherClient client = new WeatherClient();
        final LocationRepository locationRepository = new LocationRepository(sessionFactory);
        final WeatherRepository weatherRepository = new WeatherRepository(sessionFactory);
        final LocationService locationService = new LocationService(locationRepository);
        final LocationController locationController = new LocationController(locationService);
        final WeatherService weatherService = new WeatherService(client, locationService, weatherRepository);
        final WeatherController weatherController = new WeatherController(weatherService);

        final UserInterface ui = new UserInterface(locationController, weatherController);

        ui.runApplication();
    }
}
