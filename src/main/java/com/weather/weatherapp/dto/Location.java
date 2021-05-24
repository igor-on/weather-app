package com.weather.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "location")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "city_name")
    private String cityName;
    private Double latitude;
    private Double longitude;
    private String region;
    private String country;

    public Location(Long id, String cityName, Double latitude, Double longitude, String country) {
        this.id = id;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
}
