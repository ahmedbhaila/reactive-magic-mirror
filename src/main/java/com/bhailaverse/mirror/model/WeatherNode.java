package com.bhailaverse.mirror.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherNode {
    String summary;
    Double tempreature;
}
