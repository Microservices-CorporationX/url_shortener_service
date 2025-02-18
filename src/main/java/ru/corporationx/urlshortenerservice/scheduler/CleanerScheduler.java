package ru.corporationx.urlshortenerservice.scheduler;

import ru.corporationx.urlshortenerservice.model.entity.Hash;
import ru.corporationx.urlshortenerservice.repository.hash.HashRepository;
import ru.corporationx.urlshortenerservice.repository.url.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CleanerScheduler {

    private final UrlRepository urlRepository;
    private final HashRepository hashRepository;

    @Scheduled(cron = "${cleaner.scheduler.cron}")
    @Transactional
    public void cleanOldUrlAndReturnHashes() {
        List<String> freeHashesAfterDelete = urlRepository.deleteOldUrlsAndGetHashes();
        List<Hash> hashes = freeHashesAfterDelete.stream()
                .map(Hash::new)
                .toList();
        hashRepository.saveAll(hashes);
    }
}
