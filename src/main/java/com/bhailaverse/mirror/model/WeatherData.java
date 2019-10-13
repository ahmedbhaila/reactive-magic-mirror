package com.bhailaverse.mirror.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;



@Data
@Builder
public class WeatherData {
    private WeatherNode currently;
    private List<WeatherNode> future;
}
