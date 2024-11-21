package com.interview.urlshortner.controller;

import com.interview.urlshortner.model.Url;
import com.interview.urlshortner.service.MetricsService;
import com.interview.urlshortner.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UrlShortControllerTest {
    @Mock
    private UrlService urlService;

    @Mock
    private MetricsService metricsService;

    @Mock
    private HttpServletResponse servletResponse;

    @InjectMocks
    private UrlShortController urlController;



    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shortenUrl_ReturnsShortenedUrl() {
        Url url = Url.builder().longUrl("https://www.example.com").build();
        String shortUrl = "short12345";
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(urlService.getShortenUrl(url.getLongUrl())).thenReturn(shortUrl);
        ResponseEntity<Object> result = urlController.shortUrl(url);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(urlService, times(1)).getShortenUrl(url.getLongUrl());
    }

    @Test
    public void getMetrics_ReturnsDomainMetrics() {
        Map metrics = Map.of("www.example.com", 2, "www.test.com", 1);

        when(metricsService.getDomainMetrics()).thenReturn(metrics);

        Map<String, Integer> result = urlController.getMetrics();

        assertEquals(metrics, result);
        verify(metricsService, times(1)).getDomainMetrics();
    }

    @Test
    public void redirectToLongUrl_WhenShortUrlExists_ReturnsRedirectResponse() {
        String shortUrl = "short12345";
        String longUrl = "https://www.example.com";

        when(urlService.getLongUrl(shortUrl)).thenReturn(longUrl);
        urlController.redirectToLongUrl(servletResponse, shortUrl);
        verify(urlService, times(1)).getLongUrl(shortUrl);
    }

    @Test
    public void redirectToLongUrl_WhenShortUrlDoesNotExist_ReturnsNotFound() {
        String shortUrl = "short12345";
        when(urlService.getLongUrl(shortUrl)).thenReturn(null);
        urlController.redirectToLongUrl(servletResponse, shortUrl);
        verify(urlService, times(1)).getLongUrl(shortUrl);
    }
}
