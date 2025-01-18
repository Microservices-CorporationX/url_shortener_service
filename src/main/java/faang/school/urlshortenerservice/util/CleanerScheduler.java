package faang.school.urlshortenerservice.util;

import faang.school.urlshortenerservice.repository.HashRepository;
import faang.school.urlshortenerservice.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CleanerScheduler {

    private final UrlRepository urlRepository;
    private final HashRepository hashRepository;

    @Value("${app.cleaner.url-lifetime:7}")
    private int urlLifetime;

    @Transactional
    @Scheduled(cron = "${app.cleaner.cron:0 0 0 * * *}")
    public void removeOldUrls() {
        List<String> hashes = urlRepository.removeOldUrls(urlLifetime);
        hashRepository.saveHashes(hashes);
    }
}
