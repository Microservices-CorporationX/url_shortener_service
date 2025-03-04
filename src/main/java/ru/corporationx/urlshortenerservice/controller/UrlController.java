package ru.corporationx.urlshortenerservice.controller;

import ru.corporationx.urlshortenerservice.model.dto.UrlDto;
import ru.corporationx.urlshortenerservice.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class UrlController {

    private final UrlService urlService;

    @Value("${api.base-url}")
    private String baseUrl;

    @PostMapping("/url")
    public String shortenUrl(@Valid @RequestBody UrlDto urlDto) {
        String hashForUrl = urlService.shortenUrl(urlDto);

        return baseUrl + hashForUrl;
    }

    @GetMapping("/{hash}")
    public ResponseEntity<UrlDto> getUrl(@PathVariable String hash) {
        UrlDto redirectUrl = urlService.getUrl(hash);

        if (redirectUrl.getUrl() == null || redirectUrl.getUrl().isBlank()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(302)
                .header("Location", redirectUrl.getUrl())
                .build();
    }
}
