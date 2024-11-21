package com.interview.urlshortner.service;


import com.interview.urlshortner.entity.UrlEntity;
import com.interview.urlshortner.repository.UrlRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MetricsServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private MetricsService metricsService;

    public MetricsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getDomainMetrics_ReturnsTopThreeDomains() {
        List<UrlEntity> mockUrls = List.of(
                new UrlEntity("short1", "https://www.udemy.com/course1"),
                new UrlEntity("short2", "https://www.udemy.com/course2"),
                new UrlEntity("short3", "https://www.udemy.com/course3"),
                new UrlEntity("short4", "https://www.udemy.com/course4"),
                new UrlEntity("short5", "https://www.udemy.com/course5"),
                new UrlEntity("short6", "https://www.youtube.com/video1"),
                new UrlEntity("short7", "https://www.youtube.com/video2"),
                new UrlEntity("short8", "https://www.youtube.com/video3"),
                new UrlEntity("short9", "https://www.wikipedia.org/article1"),
                new UrlEntity("short10", "https://www.wikipedia.org/article2")
        );

        when(urlRepository.findAll()).thenReturn(mockUrls);
        Map<String, Integer> result = metricsService.getDomainMetrics();
        assertEquals(3, result.size());
        verify(urlRepository, times(1)).findAll();
    }
}
