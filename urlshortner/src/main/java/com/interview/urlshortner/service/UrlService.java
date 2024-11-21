package com.interview.urlshortner.service;

import com.interview.urlshortner.entity.UrlEntity;
import com.interview.urlshortner.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
public class UrlService {

    Logger logger = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    /**
     * It saves the full url to database and uses the autogenerated id to convert to Base62 string
     *
     * @param  longUrl  to be converted to shortened url
     * @return ShortUrl object
     */
    public String getShortenUrl(String longUrl) {
        Optional<UrlEntity> existingUrl = urlRepository.findByLongUrl(longUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        }

        String shortUrl = generateShortUrl(longUrl);
        urlRepository.save(new UrlEntity(shortUrl, longUrl));
        return shortUrl;
    }

    public String getLongUrl(String shortUrl) {
        Optional<UrlEntity> url = urlRepository.findByShortUrl(shortUrl);
        return url.map(UrlEntity::getLongUrl).orElse(null);
    }

    private String generateShortUrl(String longUrl) {
        return "short" + longUrl.hashCode();
    }

    public String getBaseUrl(String url) throws MalformedURLException {
        URL reqUrl = new URL(url);
        String protocol = reqUrl.getProtocol();
        String host = reqUrl.getHost();
        int port = reqUrl.getPort();

        if (port == -1) {
            return String.format("%s://%s/", protocol, host);
        } else {
            return String.format("%s://%s:%d/", protocol, host, port);
        }

    }
}
