package ru.corporationx.urlshortenerservice.repository.url;

import ru.corporationx.urlshortenerservice.model.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

    Optional<Url> findByHash(String hash);

    @Query(nativeQuery = true, value = """
            DELETE FROM url 
            WHERE created_at < (CURRENT_DATE - INTERVAL '1 year')
            RETURNING hash
            """)
    List<String> deleteOldUrlsAndGetHashes();
}
