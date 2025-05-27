package com.jay.url_shortner.Service;

import com.jay.url_shortner.Model.Url;
import com.jay.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {
    private final UrlRepository repository;
    private final String BASE_URL = "http://short.ly/";

    public UrlService(UrlRepository repository) {
        this.repository = repository;
    }

    public String shortenUrl(String longUrl, LocalDateTime expiresAt) {
        String shortCode = generateRandomCode();
        Url url = new Url();
        url.setLongUrl(longUrl);
        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(expiresAt);
        url.setVisitCount(0);
        repository.save(url);
        return BASE_URL + shortCode;
    }

    public Optional<String> getOriginalUrl(String shortCode) {
        Optional<Url> optional = repository.findByShortCode(shortCode);
        if (optional.isPresent()) {
            Url url = optional.get();
            if (url.getExpiresAt() == null || url.getExpiresAt().isAfter(LocalDateTime.now())) {
                url.setVisitCount(url.getVisitCount() + 1);
                repository.save(url);
                return Optional.of(url.getLongUrl());
            }
        }
        return Optional.empty();
    }

    private String generateRandomCode() {
        int length = 6;
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
