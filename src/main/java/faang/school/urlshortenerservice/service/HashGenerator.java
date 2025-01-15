package faang.school.urlshortenerservice.service;

import faang.school.urlshortenerservice.entity.Hash;
import faang.school.urlshortenerservice.repository.HashRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HashGenerator {

    private final HashRepository hashRepository;
    private final Base62Encoder base62Encoder;
    private final EntityManager entityManager;

    public void generateAndSaveHashes(int rangeSize) {
        long count = hashRepository.countHashes();

        int minimalHashCount = (int) (rangeSize * 0.2);

        if (count >= minimalHashCount) {
            log.info("Sufficient hashes available, skipping generation.");
            return;
        }

        log.info("Generating {} new hashes", rangeSize);

        List<Long> uniqueNumbers = generateUniqueNumbers(rangeSize);

        List<String> hashes = base62Encoder.encode(uniqueNumbers);

        for (int i = 0; i < hashes.size(); i++) {
            Hash hash = new Hash(uniqueNumbers.get(i), hashes.get(i));
            hashRepository.save(hash);
        }

        log.info("Successfully generated and saved {} hashes", hashes.size());
    }

    private List<Long> generateUniqueNumbers(int rangeSize) {
        List<Long> uniqueNumbers = new ArrayList<>();

        for (int i = 0; i < rangeSize; i++) {
            Long uniqueNumber = getNextUniqueNumber();
            uniqueNumbers.add(uniqueNumber);
        }

        return uniqueNumbers;
    }

    public Long getNextUniqueNumber() {
        Query query = entityManager.createNativeQuery("SELECT nextval('unique_number_seq')");
        return ((Number) query.getSingleResult()).longValue();
    }
}
