package faang.school.urlshortenerservice.controller;

import faang.school.urlshortenerservice.dto.UrlDto;
import faang.school.urlshortenerservice.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public String createHash(@RequestBody @Valid UrlDto url) {
        return urlService.getHash(url);
    }

    @GetMapping("/{hash}")
    public void goToLongUrl(@PathVariable(name = "hash") String hash,
                                    HttpServletResponse response) throws IOException {
        response.setStatus(302);
        response.sendRedirect(urlService.getLongUrl(hash));
    }

    @GetMapping("/some_url")
    public void checkRedirect() {
        System.out.println("checkRedirect(): success");
    }
}
