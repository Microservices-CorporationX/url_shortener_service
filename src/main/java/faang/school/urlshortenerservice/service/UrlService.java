package faang.school.urlshortenerservice.service;

import faang.school.urlshortenerservice.dto.UrlDto;
import faang.school.urlshortenerservice.entity.Url;
import faang.school.urlshortenerservice.exception.UrlNotFoundException;
import faang.school.urlshortenerservice.local_cache.HashCache;
import faang.school.urlshortenerservice.repository.RedisUrlCacheRepository;
import faang.school.urlshortenerservice.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final HashCache hashCache;
    private final RedisUrlCacheRepository redisUrlCacheRepository;
    private final UrlRepository urlRepository;

    @Transactional
    public void processUrl(UrlDto urlDto) {
        String hash = hashCache.getHash();
        Url url = Url.builder()
                .hash(hash)
                .url(urlDto.url())
                .createdAt(LocalDateTime.now())
                .build();
        urlRepository.save(url);
        redisUrlCacheRepository.save(hash, urlDto.url());
    }

    public String getUrl(String hash) {
        String url = redisUrlCacheRepository.get(hash);
        if (url != null) {
            return url;
        }
        return urlRepository.findByHash(hash).map(u -> {
            redisUrlCacheRepository.save(hash, u.getUrl());
            return u.getUrl();
        }).orElseThrow(() -> new UrlNotFoundException("Url not found for hash:" + hash));
    }
}
