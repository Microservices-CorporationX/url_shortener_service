package faang.school.urlshortenerservice.controller;

import faang.school.urlshortenerservice.dto.url.CreateUrlDto;
import faang.school.urlshortenerservice.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/shorter")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping("/shortUrl")
    public java.net.URL createShortUrl(@RequestBody CreateUrlDto dto) {
        return urlService.createNewShortUrl(dto.getUrl());
    }

    @GetMapping("/redirect/{hash}")
    public RedirectView getUrl(@PathVariable String hash) {
        return new RedirectView(urlService.getUrl(hash).toString());
    }
}
