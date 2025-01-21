package faang.school.urlshortenerservice.service.url;

import faang.school.urlshortenerservice.dto.url.UrlDto;
import faang.school.urlshortenerservice.entity.url.Url;
import faang.school.urlshortenerservice.exception.UrlException;
import faang.school.urlshortenerservice.repository.url.UrlCacheRepository;
import faang.school.urlshortenerservice.repository.url.UrlRepository;
import faang.school.urlshortenerservice.service.cache.HashCache;
import faang.school.urlshortenerservice.service.hash.HashService;
import faang.school.urlshortenerservice.validator.url.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private HashCache hashCache;

    @Mock
    private UrlValidator urlValidator;

    @Mock
    private UrlCacheRepository urlCacheRepository;

    @Mock
    private HashService hashService;

    @InjectMocks
    private UrlServiceImpl urlServiceImpl;

    private UrlDto urlDto;

    @BeforeEach
    void setUp() {
        urlDto = new UrlDto("https://example.com");
    }

    @Test
    void shortenUrl_existingUrl_returnsExistingHash() {
        Url url = Url.builder()
                .hash("existingHash")
                .url("https://example.com")
                .build();
        when(urlValidator.findByUrl(urlDto.getUrl()))
                .thenReturn(url);

        String result = urlServiceImpl.shortenUrl(urlDto);

        assertEquals("existingHash", result);
    }

    @Test
    void shortenUrl_newUrl_returnsNewHash() {
        when(urlValidator.findByUrl(urlDto.getUrl())).thenReturn(null);
        when(hashCache.getHash()).thenReturn("newHash");

        String result = urlServiceImpl.shortenUrl(urlDto);

        assertEquals("newHash", result);
        verify(urlRepository).save(any(Url.class));
        verify(urlCacheRepository).save("newHash", "https://example.com");
    }

    @Test
    void shortenUrl_noHashGenerated_throwsException() {
        when(urlValidator.findByUrl(urlDto.getUrl())).thenReturn(null);
        when(hashCache.getHash()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> urlServiceImpl.shortenUrl(urlDto));

        assertEquals("Failed to generate short URL", exception.getMessage());
    }

    @Test
    void getOriginalUrl_existingHash_returnsOriginalUrl() {
        String hash = "existingHash";
        String originalUrl = "https://example.com";
        when(urlRepository.findByHash(hash)).thenReturn(Optional.of(originalUrl));

        String result = urlServiceImpl.getOriginalUrl(hash);

        assertEquals(originalUrl, result);
        verify(urlRepository).findByHash(hash);
    }

    @Test
    void getOriginalUrl_nonExistingHash_throwsUrlException() {
        String hash = "nonExistingHash";
        when(urlRepository.findByHash(hash)).thenReturn(Optional.empty());

        UrlException exception = assertThrows(UrlException.class,
                () -> urlServiceImpl.getOriginalUrl(hash));

        assertEquals("URL not found", exception.getMessage());
        verify(urlRepository).findByHash(hash);
    }

    @Test
    void cleanExpiredUrls_expiredUrlsExist_hashesAdded() {
        Set<String> expiredHashes = Set.of("expiredHash1", "expiredHash2");
        when(urlRepository.findExpiredUrls()).thenReturn(Optional.of(expiredHashes));

        urlServiceImpl.cleanExpiredUrls();

        verify(hashService).addHashes(expiredHashes);
    }

    @Test
    void cleanExpiredUrls_noExpiredUrls_throwsUrlException() {
        when(urlRepository.findExpiredUrls()).thenReturn(Optional.empty());

        UrlException exception = assertThrows(UrlException.class,
                () -> urlServiceImpl.cleanExpiredUrls());

        assertEquals("No expired URLs found", exception.getMessage());
    }
}