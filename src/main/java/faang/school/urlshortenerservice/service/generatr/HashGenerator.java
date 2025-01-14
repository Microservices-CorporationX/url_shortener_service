package faang.school.urlshortenerservice.service.generatr;

import faang.school.urlshortenerservice.config.async.ThreadPool;
import faang.school.urlshortenerservice.properties.HashCacheQueueProperties;
import faang.school.urlshortenerservice.repository.hash.impl.HashRepositoryImpl;
import faang.school.urlshortenerservice.service.encoder.Base62Encoder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashGenerator {

    private final HashRepositoryImpl hashRepositoryImpl;
    private final HashCacheQueueProperties properties;
    private final Base62Encoder base62Encoder;
    private final ThreadPool threadPool;

    @Transactional
    @Async(value = "hashGeneratorExecutor")
    public void generateBatch(int batchSize) {
        log.info("Start generating batch of {} hashes", batchSize);
        List<Long> uniqueNumbers = hashRepositoryImpl.getUniqueNumbers(batchSize);
        List<List<Long>> batches = getBatches(uniqueNumbers);

        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        batches.forEach(batch -> futures.add(CompletableFuture.supplyAsync(() ->
                base62Encoder.encode(uniqueNumbers), threadPool.hashGeneratorExecutor()
        )));

        List<String> hashes = new ArrayList<>();
        futures.forEach(future -> hashes.addAll(future.join()));
        hashRepositoryImpl.saveHashes(hashes);
        log.info("Finished generating batch of {} hashes", batchSize);
    }

    @PostConstruct
    @Transactional
    public void initGeneration() {
        generateBatch(properties.getMaxQueueSize());
        log.info("PostConstruct method for generating new free hashes executed successfully");
    }

    private List<List<Long>> getBatches(List<Long> numbers) {
        List<List<Long>> batches = new ArrayList<>();
        int batchesQuantity = properties.getFillingBatchesQuantity();
        int batchSize = numbers.size() / batchesQuantity;
        int remainder = numbers.size() % batchesQuantity;

        int start = 0;
        for (int i = 0; i < batchesQuantity; i++) {
            int end = start + batchSize + (i < remainder ? 1 : 0);
            batches.add(new ArrayList<>(numbers.subList(start, end)));
            start = end;
        }
        return batches;
    }
}
