package com.weather.weatherapp;

import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.repository.LocationRepository;
import com.weather.weatherapp.service.LocationService;
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

        final LocationRepository locationRepository = new LocationRepository(sessionFactory);
        final LocationService locationService = new LocationService(locationRepository);
        final LocationController locationController = new LocationController(locationService);
        final UserInterface ui = new UserInterface(locationController);

        ui.runApplication();
    }
}
