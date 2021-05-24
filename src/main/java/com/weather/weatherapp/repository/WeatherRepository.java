package com.weather.weatherapp.repository;

import com.weather.weatherapp.dto.Weather;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class WeatherRepository {

    private final SessionFactory sessionFactory;

    public Weather save(Weather locationWeather) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(locationWeather);

        transaction.commit();
        session.close();

        return locationWeather;
    }
}
