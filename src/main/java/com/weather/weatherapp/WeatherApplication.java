package com.weather.weatherapp;

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

        System.out.println("develop branch");
    }
}
