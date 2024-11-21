package com.interview.urlshortner.controller;

import com.interview.urlshortner.error.InvalidUrlError;
import com.interview.urlshortner.model.ShortUrl;
import com.interview.urlshortner.model.Url;
import com.interview.urlshortner.service.MetricsService;
import com.interview.urlshortner.service.UrlService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class UrlShortController {

    Logger logger = LoggerFactory.getLogger(UrlShortController.class);

    private final UrlService urlService;
    private final MetricsService metricsService;

    public UrlShortController(UrlService urlService, MetricsService metricsService) {
        this.urlService = urlService;
        this.metricsService = metricsService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Object> shortUrl(@RequestBody Url url) {
        UrlValidator validator = new UrlValidator(
                new String[]{"http", "https"}
        );
        String longUrl = url.getLongUrl();
        if (!validator.isValid(longUrl)) {
            logger.error("Malformed Url provided");
            InvalidUrlError error = new InvalidUrlError("url", url.getLongUrl(), "Invalid URL");
            return ResponseEntity.badRequest().body(error);
        }
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        ShortUrl shortUrl = ShortUrl.builder().shortUrl(baseUrl + "/" + urlService.getShortenUrl(longUrl)).build();
        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
    }

    @GetMapping("/{shortenString}")
    public void redirectToLongUrl(HttpServletResponse response, @PathVariable String shortenString) {
        try {
            String longUrl = urlService.getLongUrl(shortenString);
            logger.info("long url is {}", longUrl);
            if(StringUtils.isNotEmpty(longUrl)) {
                response.sendRedirect(longUrl);
            }
        } catch (NoSuchElementException e) {
            logger.error("No URL found for {} in the db", shortenString);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            logger.error("Could not redirect to the full url");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }

    @GetMapping("/metrics")
    public Map<String, Integer> getMetrics() {
        return metricsService.getDomainMetrics();
    }
}
