package com.weather.weatherapp.repository;

import com.weather.weatherapp.dto.Weather;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Weather> findAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Weather> allWeathers = session.createQuery("FROM Weather", Weather.class).getResultList();

        transaction.commit();
        session.close();

        return allWeathers;
    }

    public List<Object[]> findStatisticalWeatherDataFromLastMonthByCityName(String cityName) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();

        final List<Object[]> resultList = session.createQuery("SELECT cityName, AVG(temperature), AVG(humidity), AVG(pressure), AVG(windSpeed), AVG(windDegree) " +
                "FROM Weather w " +
                "WHERE cityName = :cityName " +
                "AND dateTime >= :dateTime " +
                "GROUP BY cityName", Object[].class)
                .setParameter("cityName", cityName)
                .setParameter("dateTime", LocalDateTime.now().minusMonths(1))
                .getResultList();

        transaction.commit();
        session.close();

        return resultList;
    }
}
