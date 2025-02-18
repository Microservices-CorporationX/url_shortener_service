package ru.corporationx.urlshortenerservice.service;

import ru.corporationx.urlshortenerservice.cache.HashCache;
import ru.corporationx.urlshortenerservice.model.dto.UrlDto;
import ru.corporationx.urlshortenerservice.model.entity.Url;
import ru.corporationx.urlshortenerservice.exception.UrlNotFoundException;
import ru.corporationx.urlshortenerservice.repository.url.UrlCacheRepository;
import ru.corporationx.urlshortenerservice.repository.url.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlCacheRepository urlCacheRepository;
    private final UrlRepository urlRepository;
    private final HashCache hashCache;

    public String shortenUrl(UrlDto urlDto) {
        String hash = hashCache.getHash();
        Url url = Url.builder()
                .hash(hash)
                .url(urlDto.getUrl())
                .build();

        urlCacheRepository.save(hash, urlDto.getUrl());
        urlRepository.save(url);

        return hash;
    }

    public UrlDto getUrl(String hash) {
        String url = urlCacheRepository.getByHash(hash);
        if (url != null && !url.isBlank()) {
            return new UrlDto(url);
        }

        Url urlFromRepo = urlRepository.findByHash(hash)
                .orElseThrow(() -> new UrlNotFoundException("URL does not exist for this hash"));

        return new UrlDto(urlFromRepo.getUrl());
    }

}
