package faang.school.urlshortenerservice.util;

import faang.school.urlshortenerservice.service.HashCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Warmup implements CommandLineRunner {
    private final HashGenerator hashGenerator;
    private final HashCacheService hashCacheService;

    @Override
    public void run(String... args) {
        hashGenerator.asyncHashRepositoryRefill().thenRun(hashCacheService::asyncCacheRefill).join();
        log.info("Warmup finished correctly, HashRepository and HashCache have enough data.");
    }
}
