package faang.school.urlshortenerservice.controller;

import faang.school.urlshortenerservice.dto.LongUrlDto;
import faang.school.urlshortenerservice.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "URL Shortener API", description = "Endpoints to use the service")
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/url")
    @Operation(summary = "Convert long URL into short URL")
    public ResponseEntity<String> create(@Valid @RequestBody LongUrlDto longUrlDto) {
        log.info("Request to convert long URL '{}' received", longUrlDto.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.createShortUrl(longUrlDto));
    }

    @GetMapping("/{hash}")
    @Operation(summary = "Get real url matching provided short url")
    public ResponseEntity<Void> getUrl(@PathVariable @Size(max = 6, message = "Invalid hash size") String hash) {
        log.debug("Request to get real URL matching hash '{}' received", hash);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, urlService.getUrl(hash))
                .build();
    }
}