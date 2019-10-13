package com.bhailaverse.mirror.service;

import com.bhailaverse.mirror.model.News;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class NewApiService implements NewsService {

    private WebClient webClient;

    @Value("${news.api.key}")
    String apiKey;

    @Value("${news.api.url}")
    String url;

    @Value("${news.api.uri")
    String uri;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("uri", url))
                .build();
    }

    @Override
    public Mono<List<News>> getTopHeadlines() {
        LinkedMultiValueMap bodyProps = new LinkedMultiValueMap();
        bodyProps.add("country", "us");
        bodyProps.add("apiKey", apiKey);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(uri)
                        .queryParam("country", "us")
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonResponse -> Configuration.defaultConfiguration().jsonProvider().parse(jsonResponse))
                .map(document -> convertToNewsData(document));

    }

    private List<News> convertToNewsData(Object document) {

        Mono.just(document).flatMap(doc -> (JsonPath.read(doc, "$..title"))).subscribe(s -> System.out.println(s));
                //.flatMapIterable(title -> Observable.just(new News((String)title)));
        return null;
    }
}
