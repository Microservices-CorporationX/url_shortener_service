package faang.school.urlshortenerservice.repository.redis;

import faang.school.urlshortenerservice.entity.Url;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlCacheRepository extends CrudRepository<Url, String> {
}