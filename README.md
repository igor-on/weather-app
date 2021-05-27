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
* /locations GET POST
* /locations/{cityname}
* /locations/{id} DELETE  
* /weathers POST
* /forecast POST
* /statistical/{cityname}  
* /locations/writeToFile
* /weathers/writeToFile
* /locations/readFromFile
* /weathers/readFromFile

## Technologies
* JAVA 11
* Hibernate 5.4.30
* Lombok 1.18.20
* Junit5 5.7.1
* AssertJ 3.19
* Mockito 3.7.7

## Setup

```
very soon...
```