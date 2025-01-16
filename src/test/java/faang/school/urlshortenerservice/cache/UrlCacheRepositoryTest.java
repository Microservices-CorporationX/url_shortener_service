package faang.school.urlshortenerservice.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlCacheRepositoryTest {
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private UrlCacheRepository urlCacheRepository;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void saveUrlShouldSaveUrlInRedisWhenCalledWithHashAndLongUrl() {
        String hash = "abc123";
        String longUrl = "https://example.com/some/long/url";

        urlCacheRepository.saveUrl(hash, longUrl);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).set(hash, longUrl, Duration.ofDays(30));
    }

    @Test
    void getUrlShouldReturnUrlWhenHashExists() {
        String hash = "abc123";
        String expectedUrl = "https://example.com";
        when(valueOperations.get(hash)).thenReturn(expectedUrl);

        String actualUrl = urlCacheRepository.getUrl(hash);

        assertEquals(expectedUrl, actualUrl);
        verify(valueOperations, times(1)).get(hash);
    }

    @Test
    void getUrlShouldReturnNullWhenHashDoesNotExist() {
        String hash = "abc123";
        when(valueOperations.get(hash)).thenReturn(null);

        String actualUrl = urlCacheRepository.getUrl(hash);

        assertEquals(null, actualUrl);
        verify(valueOperations, times(1)).get(hash);
    }
}
