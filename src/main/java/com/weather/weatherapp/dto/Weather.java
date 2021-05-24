package com.weather.weatherapp.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "city_name")
    private String cityName;
    private Double temperature;
    private  Double humidity;
    private Double pressure;
    @Column(name = "wind_speed")
    private Double windSpeed;
    @Column(name = "wind_degree")
    private Double windDegree;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
