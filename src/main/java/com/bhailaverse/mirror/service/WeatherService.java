package com.bhailaverse.mirror.service;

import com.bhailaverse.mirror.model.WeatherData;
import reactor.core.publisher.Mono;

public interface WeatherService {
    public Mono<WeatherData> getForecast(String lat, String lng);
}
