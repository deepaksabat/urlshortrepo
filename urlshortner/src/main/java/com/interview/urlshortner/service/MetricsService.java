package com.interview.urlshortner.service;

import com.interview.urlshortner.entity.UrlEntity;
import com.interview.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final UrlRepository urlRepository;

    public MetricsService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    public Map<String, Integer> getDomainMetrics() {
        List<UrlEntity> allUrls = urlRepository.findAll();
        Map<String, Integer> domainCountMap = new HashMap<>();
        for (UrlEntity url : allUrls) {
            String domain = extractDomain(url.getLongUrl());
            domainCountMap.put(domain, domainCountMap.getOrDefault(domain, 0) + 1);
        }
        return domainCountMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private String extractDomain(String url) {
        return url.split("/")[2];
    }
}
