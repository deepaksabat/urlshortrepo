package com.interview.urlshortner.service;

import com.interview.urlshortner.entity.UrlEntity;
import com.interview.urlshortner.model.Url;
import com.interview.urlshortner.repository.UrlRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    public UrlServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shortenUrl_WhenUrlExists_ReturnsExistingShortUrl() {
        String longUrl = "https://www.example.com";
        String shortUrl = "short.ly/12345";

        when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.of(new UrlEntity(shortUrl, longUrl)));

        String result = urlService.getShortenUrl(longUrl);

        assertEquals(shortUrl, result);
        verify(urlRepository, times(1)).findByLongUrl(longUrl);
        verify(urlRepository, never()).save(any());
    }

    @Test
    public void shortenUrl_WhenUrlDoesNotExist_GeneratesAndSavesShortUrl() {
        String longUrl = "https://www.example.com";

        when(urlRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String result = urlService.getShortenUrl(longUrl);

        assertTrue(result.startsWith("short"));
        verify(urlRepository, times(1)).findByLongUrl(longUrl);
        verify(urlRepository, times(1)).save(any());
    }

    @Test
    public void getLongUrl_WhenShortUrlExists_ReturnsLongUrl() {
        String shortUrl = "short12345";
        String longUrl = "https://www.example.com";

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(new UrlEntity(shortUrl, longUrl)));

        String result = urlService.getLongUrl(shortUrl);

        assertEquals(longUrl, result);
        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    public void getLongUrl_WhenShortUrlDoesNotExist_ReturnsNull() {
        String shortUrl = "short12345";

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        String result = urlService.getLongUrl(shortUrl);

        assertNull(result);
        verify(urlRepository, times(1)).findByShortUrl(shortUrl);
    }
}
