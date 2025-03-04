package ru.corporationx.urlshortenerservice.service;

import ru.corporationx.urlshortenerservice.cache.HashCache;
import ru.corporationx.urlshortenerservice.exception.UrlNotFoundException;
import ru.corporationx.urlshortenerservice.model.dto.UrlDto;
import ru.corporationx.urlshortenerservice.model.entity.Url;
import ru.corporationx.urlshortenerservice.repository.url.UrlCacheRepository;
import ru.corporationx.urlshortenerservice.repository.url.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlCacheRepository urlCacheRepository;
    @Mock
    private UrlRepository urlRepository;
    @Mock
    private HashCache hashCache;
    @InjectMocks
    private UrlService urlService;
    private String hash;
    private String url;

    @BeforeEach
    public void setUp() {
        hash = "Jbd245";
        url = "https://corp.x/";
    }

    @Test
    public void testShortenUrl() {
        UrlDto urlDto = prepareDto();
        Url url = prepareEntity(hash, urlDto.getUrl());
        when(hashCache.getHash()).thenReturn(hash);

        String actualHash = urlService.shortenUrl(urlDto);

        verify(urlCacheRepository).save(hash, urlDto.getUrl());
        verify(urlRepository).save(url);
        assertEquals(hash, actualHash);
    }

    @Test
    public void testGetUrlIfCacheReturnUrl() {
        when(urlCacheRepository.getByHash(hash)).thenReturn(url);

        UrlDto actualUrl = urlService.getUrl(hash);

        assertEquals(url, actualUrl.getUrl());
    }

    @Test
    public void testGetUrlIfUrlRepositoryReturnUrl() {
        Url urlFromRepo = prepareEntity(hash, url);
        when(urlCacheRepository.getByHash(hash)).thenReturn(null);
        when(urlRepository.findByHash(hash)).thenReturn(Optional.of(urlFromRepo));

        UrlDto actualUrl = urlService.getUrl(hash);

        assertEquals(url, actualUrl.getUrl());
    }

    @Test
    public void testGetUrlIfUrlRepositoryDoesNotReturnUrl() {
        when(urlCacheRepository.getByHash(hash)).thenReturn(null);
        when(urlRepository.findByHash(hash)).thenReturn(Optional.empty());

        UrlNotFoundException exception = assertThrows(UrlNotFoundException.class,
                () -> urlService.getUrl(hash));

        verify(urlRepository).findByHash(anyString());
        assertEquals("URL does not exist for this hash", exception.getMessage());
    }

    private UrlDto prepareDto() {
        return new UrlDto(url);
    }

    private Url prepareEntity(String hash, String url) {
        return Url.builder()
                .hash(hash)
                .url(url)
                .build();
    }
}
