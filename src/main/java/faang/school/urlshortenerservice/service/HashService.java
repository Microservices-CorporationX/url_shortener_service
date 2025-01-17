package faang.school.urlshortenerservice.service;

import faang.school.urlshortenerservice.config.UrlShortenerProperties;
import faang.school.urlshortenerservice.entity.Hash;
import faang.school.urlshortenerservice.repository.HashRepository;
import faang.school.urlshortenerservice.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class HashService {
    private final HashRepository hashRepository;
    private final Base62Encoder base62Encoder;
    private final UrlShortenerProperties urlShortenerProperties;

    @Async("hashServiceExecutor")
    public CompletableFuture<Void> uploadHashesInDatabaseIfNecessary() {
        if (!isEnoughHashCapacity()) {
            List<Hash> hashes = generateBatch(urlShortenerProperties.hashAmountToGenerate());
            hashRepository.saveAll(hashes);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("hashServiceExecutor")
    public CompletableFuture<List<Hash>> getHashesFromDatabase() {
        return CompletableFuture.completedFuture(hashRepository.getHashes(urlShortenerProperties.hashAmountToLocalCache()));
    }

    private List<Hash> generateBatch(Long amountOfNumbersFromSequence) {
        List<Long> numbersToDecode = hashRepository.getUniqueNumbersFromSequence(amountOfNumbersFromSequence);

        return numbersToDecode.parallelStream()
                .map(base62Encoder::encode)
                .map(Hash::new)
                .toList();
    }

    private boolean isEnoughHashCapacity() {
        long lowerBoundCapacity = (long) (urlShortenerProperties.hashAmountToGenerate() * urlShortenerProperties.hashDatabaseThresholdRatio());
        return hashRepository.count() >= lowerBoundCapacity;
    }
}
