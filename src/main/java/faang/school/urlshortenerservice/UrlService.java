package faang.school.urlshortenerservice;

import faang.school.urlshortenerservice.dto.UrlDto;
import faang.school.urlshortenerservice.exception.DataNotFoundException;
import faang.school.urlshortenerservice.hesh.HashCache;
import faang.school.urlshortenerservice.repository.HashRepository;
import faang.school.urlshortenerservice.repository.RedisCacheRepository;
import faang.school.urlshortenerservice.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final HashCache hashCoach;
    private final HashRepository hashRepository;
    private final UrlRepository urlRepository;
    private final RedisCacheRepository redisCacheRepository;
    @Value("${url.original-path}")
    private String urlPath;

    @Transactional
    public String createShortUrl(UrlDto urlDto) {
        String hash = hashCoach.getHash();
        if (hash == null || hash.isBlank()) {
            throw new IllegalStateException("Не удалось сгенерировать хэш. Хэш не может быть нулевым или пустым.");
        }
        log.info("Generated hash: {}", hash);

        urlRepository.saveUrl(hash, urlDto.url());
        redisCacheRepository.save(hash, urlDto.url());
        return urlPath.concat(hash);
    }

    @Transactional
    public String getOriginalUrl(String hash){
        String cashedUrl = redisCacheRepository.get(hash);
        if (cashedUrl != null) {
            return cashedUrl;
        }
        try {
            cashedUrl = urlRepository.getUrlByHash(hash);
        }catch (EmptyResultDataAccessException e){
            throw new DataNotFoundException("Url with hash %s was not found in database".formatted(hash));
        }
        return cashedUrl;
    }

}
