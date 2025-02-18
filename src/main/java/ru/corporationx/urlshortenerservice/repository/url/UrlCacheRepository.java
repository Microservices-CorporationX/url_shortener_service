package ru.corporationx.urlshortenerservice.repository.url;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UrlCacheRepository  {

    @Value("${hash.cache.time-to-live:1}")
    private long timeToLiveInDays;

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String hash, String url) {
        redisTemplate.opsForValue().set(hash, url);
        redisTemplate.expire(hash, timeToLiveInDays, TimeUnit.DAYS);
    }

    public String getByHash(String hash) {
        return (String) redisTemplate.opsForValue().get(hash);
    }
}

