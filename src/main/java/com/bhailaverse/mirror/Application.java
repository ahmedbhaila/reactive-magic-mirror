package com.bhailaverse.mirror;

import com.bhailaverse.mirror.model.WeatherData;
import com.bhailaverse.mirror.service.DarkskyWeatherService;
import com.bhailaverse.mirror.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    DarkskyWeatherService darkskyWeatherService;

    @Autowired
    NewsService newsService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(darkskyWeatherService.getForecast("37.334403", "-121.963176").block().toString());
        //newsService.getTopHeadlines().block();
    }
}
