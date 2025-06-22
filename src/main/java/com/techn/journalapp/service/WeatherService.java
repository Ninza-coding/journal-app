package com.techn.journalapp.service;

import com.techn.journalapp.api.response.WeatherResponse;
import com.techn.journalapp.cache.AppCache;
import com.techn.journalapp.constants.PlaceHolders;
import com.techn.journalapp.enums.Weather_City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;


    // private static final String API="https://api.weatherstack.com/current?access_key=API_KEY&query=city";

    @Autowired
    private AppCache appCache;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get( Weather_City.weather_of_+ city, WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        } else {
            String finalApi = appCache.APP_CACHE.get(AppCache.keys.WEATHER_API.toString()).replace(PlaceHolders.CITY, city).replace(PlaceHolders.API_KEY, apiKey);
            System.out.println(finalApi);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class);

            WeatherResponse body = response.getBody();
            if (body != null) {
                redisService.set(Weather_City.weather_of_+ city, body, 300l);
            }
            return body;
        }

    }
}
