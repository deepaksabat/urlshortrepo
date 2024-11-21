package com.interview.urlshortner.service;

import com.interview.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MetricsService {

    private final UrlRepository urlRepository;

    public MetricsService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Map<String, Integer> getDomainMetrics() {
        Map<String, Integer> domainCount = new HashMap<>();
        urlRepository.findAll().forEach(url -> {
            String domain = extractDomain(url.getLongUrl());
            domainCount.put(domain, domainCount.getOrDefault(domain, 0) + 1);
        });
        return domainCount;
    }

    private String extractDomain(String url) {
        return url.split("/")[2];
    }
}
