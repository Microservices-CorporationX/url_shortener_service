package faang.school.urlshortenerservice.controller;

import faang.school.urlshortenerservice.dto.UrlRequestDto;
import faang.school.urlshortenerservice.service.UrlService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Data
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/url")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createShortUrl(@RequestBody @Validated UrlRequestDto urlRequestDto) {
        String shortUrl = urlService.createShortUrl(urlRequestDto.getLongUrl());
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String hash) {
        String originalUrl = urlService.getOriginalUrl(hash);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();
    }
}
