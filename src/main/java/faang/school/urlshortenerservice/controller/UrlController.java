package faang.school.urlshortenerservice.controller;

import faang.school.urlshortenerservice.dto.UrlDto;
import faang.school.urlshortenerservice.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("${short-url.base-path}")
@Controller
@RequiredArgsConstructor
@Validated
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<String> createShortUrl(@Valid @RequestBody UrlDto urlDto) {
        return ResponseEntity.ok(urlService.createShortUrl(urlDto));
    }

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("/{hash}")
    public String redirectToOriginalUrl(@PathVariable @Length(max = 6) String hash) {
        String originalUrl = urlService.getOriginalUrl(hash);
        return "redirect:%s".formatted(originalUrl);
    }
}
