package faang.school.urlshortenerservice.cache;

import faang.school.urlshortenerservice.entity.Hash;
import faang.school.urlshortenerservice.exception.CacheUpdateException;
import faang.school.urlshortenerservice.exception.HashRetrievalException;
import faang.school.urlshortenerservice.generator.HashGenerator;
import faang.school.urlshortenerservice.repository.HashRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class HashCache {
    @Value("${hash-cache.queue-capacity}")
    private int queueCapacity;
    @Value("${hash-cache.percent}")
    private double percent;
    @Value("${hash-cache.redis-batch-size}")
    private int redisBatchSize;

    private final AtomicBoolean isCacheFilling = new AtomicBoolean(false);
    private final ThreadPoolTaskExecutor taskExecutor;
    private final HashGenerator hashGenerator;
    private final HashRepository hashRepository;
    private BlockingQueue<Hash> caches;

    @PostConstruct
    public void init() {
        caches = new ArrayBlockingQueue<>(queueCapacity);
        try {
            hashGenerator.generateBatch();
            caches.addAll(hashRepository.getHashBatch(redisBatchSize));
        } catch (Exception e) {
            log.error("Failed to initialize cache during startup", e);
            throw new IllegalStateException("Failed to initialize cache during startup", e);
        }
    }

    public Hash getHash() {
        if (caches.size() <= redisBatchSize * percent) {
            if (isCacheFilling.compareAndSet(false, true)) {
                CompletableFuture.runAsync(() -> {
                    try {
                        hashGenerator.generateBatch();
                        caches.addAll(hashRepository.getHashBatch(redisBatchSize));
                    } catch (Exception e) {
                        throw new CacheUpdateException("Failed to update cache with new hashes: " + e.getMessage(), e);
                    } finally {
                        isCacheFilling.set(false);
                    }
                }, taskExecutor);
            }
        }
        try {
            return caches.take();
        } catch (InterruptedException e) {
            throw new HashRetrievalException("Failed to retrieve hash from the queue: " + e.getMessage(), e);
        }
    }
}
