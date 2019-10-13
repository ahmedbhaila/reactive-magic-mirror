package com.bhailaverse.mirror.service;

import com.bhailaverse.mirror.model.News;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NewsService {
    Mono<List<News>> getTopHeadlines();
}
