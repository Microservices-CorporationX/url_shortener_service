package faang.school.urlshortenerservice.repository;

import faang.school.urlshortenerservice.entity.HashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HashRepository extends JpaRepository<HashEntity, String> {
    @Query(value = "SELECT nextval('unique_number_seq') FROM generate_series(1, :number)", nativeQuery = true)
    List<Long> getUniqueNumbers(int number);

    @Query(value = "SELECT hash FROM hash WHERE is_used = false", nativeQuery = true)
    List<String> getAvailableHashes();

    HashEntity findByHash(String hash);

    @Modifying
    @Transactional
    @Query(value = "UPDATE hash SET is_used = false WHERE hash IN :hashes", nativeQuery = true)
    void saveUnusedHashes(List<String> hashes);

    @Modifying
    @Transactional
    @Query(value = "UPDATE hash SET is_used = true WHERE h.hash = :hash", nativeQuery = true)
    void markHashAsUsed(String hash);
}
