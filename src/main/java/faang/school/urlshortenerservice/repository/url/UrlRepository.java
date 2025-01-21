package faang.school.urlshortenerservice.repository.url;

import faang.school.urlshortenerservice.entity.url.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

    @Query("SELECT u FROM Url u WHERE u.url = :url")
    Optional<Url> findByUrl(@Param("url") String url);

    @Query("SELECT u.url FROM Url u WHERE u.hash = :hash")
    Optional<String> findByHash(@Param("hash") String hash);
}
