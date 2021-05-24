package com.weather.weatherapp.repository;

import com.weather.weatherapp.dto.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
}
