package faang.school.urlshortenerservice.service;

import faang.school.urlshortenerservice.model.Url;
import faang.school.urlshortenerservice.redis.UrlCache;
import faang.school.urlshortenerservice.repository.UrlJpaRepository;
import faang.school.urlshortenerservice.service.hash.HashCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    @Value("${server.host}")
    private String host;
    @Value("${server.port}")
    private int port;
    private final HashCache hashCache;
    private final UrlJpaRepository urlRepository;
    private final UrlCache urlCache;


    public String shortenUrl(String url) {
        Url shortUrl = urlRepository.findByUrl(url)
                .orElseGet(() -> Url.builder()
                        .url(url)
                        .hash(hashCache.getHash().toString())
                        .build());

        shortUrl.setLastReceivedAt(now());

        shortUrl = urlRepository.save(shortUrl);
        urlCache.saveInCache(shortUrl);

        return String.format("%s:%s/%s", host, port, shortUrl.getHash());
    }
}
