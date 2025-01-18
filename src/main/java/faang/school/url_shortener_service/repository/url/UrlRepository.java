package faang.school.url_shortener_service.repository.url;

import faang.school.url_shortener_service.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {
    @Modifying
    @Query(nativeQuery = true, value = """
            DELETE FROM url
            WHERE created_at <  NOW() - INTERVAL '1 year'
            RETURNING *
            """)
    List<Url> deleteOldUrls();

    boolean existsByUrl(String url);
}