package faang.school.urlshortenerservice.repozitory;

import faang.school.urlshortenerservice.entity.Hash;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashRepository extends CrudRepository<Hash, Long> {

    @Query(nativeQuery = true, value = """
            SELECT nextval('unique_number_seq') FROM generate_series(1, :maxRange)
            """)
    List<Long> getUniqueNumbers(@Param("maxRange") long maxRange);

    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO hash (hash) VALUES (:hashes)
            """)
    void save(@Param("hashes") List<String> hashes);

    @Query(nativeQuery = true, value = """
            DELETE FROM hash WHERE id IN(
                SELECT id FROM hash ORDER BY RANDOM() LIMIT :amount
            )
            RETURNING *;
            """)
    List<Hash> getHashBatch(@Param("amount") long amount);
}
