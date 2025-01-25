package faang.school.urlshortenerservice.repozitory;

import faang.school.urlshortenerservice.entity.Hash;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashRepository extends CrudRepository<Hash, Long> {

    @Query(nativeQuery = true, value = """
            SELECT nextval('unique_number_seq') FROM generate_series(1, :batchSize)
            """)
    List<Long> getUniqueNumbers(@Param("batchSize") long batchSize);


    List<Hash> saveAll(List<Hash> hashes);

    @Query(nativeQuery = true, value = """
            DELETE FROM hash WHERE id IN(
                SELECT id FROM hash ORDER BY RANDOM() LIMIT :batchSize
            )
            RETURNING *;
            """)
    List<Hash> getHashBatch(@Param("batchSize") long batchSize);
}
