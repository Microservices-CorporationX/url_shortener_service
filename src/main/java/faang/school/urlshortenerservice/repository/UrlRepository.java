package faang.school.urlshortenerservice.repository;

import faang.school.urlshortenerservice.entity.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

    Optional<Url> findByHash(String hash);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM urls
            WHERE created_at < NOW() - INTERVAL '1 YEAR'
            RETURNING hash
            """, nativeQuery = true)
    List<String> deleteUrlsOlderThanOneYear();
}
