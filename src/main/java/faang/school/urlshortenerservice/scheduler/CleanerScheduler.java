package faang.school.urlshortenerservice.scheduler;

import faang.school.urlshortenerservice.entity.Hash;
import faang.school.urlshortenerservice.repository.HashRepository;
import faang.school.urlshortenerservice.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CleanerScheduler {

    private final HashRepository hashRepository;
    private final UrlRepository urlRepository;

    @Scheduled(cron = "${scheduler.cleaning.url.cron}")
    @Transactional
    public void cleaningUrl() {
        List<String> stringHashes = urlRepository.findHashesToDelete();
        urlRepository.deleteExpiredUrls();
        List<Hash> hashes = stringHashes.stream().map(Hash::new).toList();

        hashRepository.saveAll(hashes);
    }

}
