package com.weather.weatherapp.repository;

import com.weather.weatherapp.dto.Location;
import com.weather.weatherapp.exception.NoLocationFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

@RequiredArgsConstructor
public class LocationRepository {

    private final SessionFactory sessionFactory;

    public Location save(Location location) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(location);

        transaction.commit();
        session.close();

        return location;
    }

    public void remove(Location location) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.remove(location);

        transaction.commit();
        session.close();
    }

    public Location find(Long id) throws NoLocationFoundException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Optional<Location> foundLocation = Optional.ofNullable(session.find(Location.class, id));

        transaction.commit();
        session.close();

        return foundLocation.orElseThrow(() -> new NoLocationFoundException("There is no saved location with given id: " + id));
    }

    public Location findByName(String cityName) throws NoLocationFoundException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Optional<Location> foundLocation = session.createQuery("FROM Location WHERE cityName = :cityName", Location.class)
                .setParameter("cityName", cityName)
                .uniqueResultOptional();

        transaction.commit();
        session.close();

        return foundLocation.orElseThrow(() -> new NoLocationFoundException("There is no saved location with given city name: " + cityName));
    }
}
