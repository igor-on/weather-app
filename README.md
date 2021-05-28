# WEATHER APP

REST API weather app created using Java 11 and Hibernate only, additionally a terminal UI

## TABLE OF CONTENTS

* [What you can do in this app](#what-you-can-do)
* [Prepared enpoints](#prepared-endpoints)
* [Technologies](#technologies)
* [Setup](#setup)

## What you can do in this app

* CRUD methods for locations
* find location by city name

### After you add some locations:

* check current weather and save it for later calculations
* get forecast up to 7 days
* get statistical weather from last month

### Once you store few locations in data:

* write these to Json file for later usage
* later usage has come, read data from Json file and start again!

## Prepared endpoints

The request body needs to be in Json format

* GET `/locations`
* POST `/locations`
  ##### Example:
   ```
  {
    "cityName": "Miami",
    "latitude": 25.7743,
    "longitude": -80.1937,
    "region": null,
    "country": "US"
  }
   ```
* PUT `/locations`
  #### Example:
  ```  
  {                             
    "id": 1,        
    "cityName": "Miamiiii"
  }        
  ```     
  #### or
  ```                       
  {                         
   "id": 1,              
   "latitude": 30.99,
   "longitude": -83.20  
  }                         
  ```  
* DELETE `/locations/{id}`
* GET`/locations/{cityname}`
* POST `/weathers`
  ##### Example:
   ```
  { "cityName": "Miami" }
   ```
  #### or
   ```
  { "id": 1 }
   ```

* POST `/forecast`
  ##### Example:
   ```
  {
    "cityName": "Miami",
    "selectedDate": "2021-05-22"
  }
  ```
* GET `/statistics/{cityname}`
* POST `/locations/writeToFile`
* POST `/weathers/writeToFile`
* POST `/locations/readFromFile`
* POST `/weathers/readFromFile`

  #### Example for all file functions:
   ```
  { "fileName": "my locations" }
   ```

## Technologies

* JAVA 11
* Hibernate 5.4.30
* Lombok 1.18.20
* Junit5 5.7.1
* AssertJ 3.19
* Mockito 3.7.7

## How to start

```
git clone https://github.com/igor-on/weather-app.git
```

### After you clone repository

* You need to create the appropriate tables in mySql data base and add credentials in hibernate.cfg.xml file
* Then you need to add your API keys in URILibrary class
    * FIRST_API_KEY - OpenWeatherMap
    * SECOND_API_KEY - WeatherStack 