package faang.school.urlshortenerservice.repository.hash;

import faang.school.urlshortenerservice.entity.hash.Hash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashRepository extends JpaRepository<Hash, String> {

    @Query(nativeQuery = true, value = """
            DELETE FROM hash h WHERE
            EXISTS (SELECT * FROM hash LIMIT :limit)
            RETURNING *
            """)
    @Modifying
    List<Hash> getHashBatch(@Param("limit") int limit);
}
