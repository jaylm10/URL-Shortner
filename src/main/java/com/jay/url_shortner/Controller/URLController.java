package com.jay.urlshortener.controller;

import com.jay.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> payload) {
        String longUrl = payload.get("longUrl");
        String expiration = payload.get("expiresAt"); // optional
        LocalDateTime expiresAt = expiration != null ? LocalDateTime.parse(expiration) : null;
        String shortUrl = urlService.shortenUrl(longUrl, expiresAt);
        return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortCode) {
        return urlService.getOriginalUrl(shortCode)
                .map(url -> ResponseEntity.status(302).location(URI.create(url)).build())
                .orElse(ResponseEntity.notFound().build());
    }
}
