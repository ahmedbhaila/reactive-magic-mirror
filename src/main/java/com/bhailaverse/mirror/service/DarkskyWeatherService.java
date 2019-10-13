package com.bhailaverse.mirror.service;

import com.bhailaverse.mirror.model.WeatherData;
import com.bhailaverse.mirror.model.WeatherNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@Component
public class DarkskyWeatherService implements WeatherService {

    private WebClient webClient;
    private String apiKey;

    @Autowired
    public DarkskyWeatherService(@Value("${darksky.weather.service.url}") String url, @Value("${darksky.weather.api.key}") String apiKey) {
        this.apiKey = apiKey;

        webClient = WebClient
                .builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                //.defaultUriVariables(Collections.singletonMap("uri", url))
                .build();
    }

    @Override
    public Mono<WeatherData> getForecast(String lat, String lng) {
        return webClient
                .get()
                .uri("/forecast/{apiKey}/{geo}", apiKey, lat + "," + lng).retrieve()
                .bodyToMono(String.class)
                .map(jsonResponse -> Configuration.defaultConfiguration().jsonProvider().parse(jsonResponse))
                .map(document -> convertToWeatherData(document));

    }

    private WeatherData convertToWeatherData(Object document) {
        WeatherData.WeatherDataBuilder weatherDataBuilder = WeatherData.builder();

        Mono.just(document).map(doc -> new WeatherNode(JsonPath.read(doc, "$.currently.summary"), JsonPath.read(doc, "$.currently.temperature")))
                .subscribe(node -> weatherDataBuilder.currently(node));

        Mono.just(JsonPath.read(document, "$.daily.data"))
                .map(object -> (JSONArray) object)
                .flatMapIterable(array -> array)
                .map(element -> (LinkedHashMap) element)
                .map(element -> new WeatherNode((String)element.get("summary"), (Double) element.get("temperatureHigh")))
                .collectList()
                .subscribe(nodes -> weatherDataBuilder.future(nodes));


        return weatherDataBuilder.build();
    }
}
