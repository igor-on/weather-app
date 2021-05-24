package com.weather.weatherapp;

import com.sun.net.httpserver.HttpServer;
import com.weather.weatherapp.client.WeatherClient;
import com.weather.weatherapp.controller.FileController;
import com.weather.weatherapp.controller.LocationController;
import com.weather.weatherapp.controller.WeatherController;
import com.weather.weatherapp.endpoint.LocationEndpoints;
import com.weather.weatherapp.repository.LocationRepository;
import com.weather.weatherapp.repository.WeatherRepository;
import com.weather.weatherapp.service.FileService;
import com.weather.weatherapp.service.LocationService;
import com.weather.weatherapp.service.WeatherService;
import com.weather.weatherapp.ui.UserInterface;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WeatherApplication {
    public static void main(String[] args) throws IOException {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        final WeatherClient client = new WeatherClient();
        final LocationRepository locationRepository = new LocationRepository(sessionFactory);
        final WeatherRepository weatherRepository = new WeatherRepository(sessionFactory);
        final LocationService locationService = new LocationService(locationRepository);
        final LocationController locationController = new LocationController(locationService);
        final WeatherService weatherService = new WeatherService(client, locationService, weatherRepository);
        final WeatherController weatherController = new WeatherController(weatherService);
        final FileService fileService = new FileService(locationService, weatherService);
        final FileController fileController = new FileController(fileService);


        final LocationEndpoints locationEndpoints = new LocationEndpoints(locationController, server);

        locationEndpoints.runApp();
        server.start();


//        final UserInterface ui = new UserInterface(locationController, weatherController, fileController);
//        ui.runApplication();
    }
}
