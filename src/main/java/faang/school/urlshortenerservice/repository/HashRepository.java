package faang.school.urlshortenerservice.repository;

import faang.school.urlshortenerservice.entity.Hash;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashRepository extends JpaRepository<Hash, Long> {
    @Query(nativeQuery = true, value = """
            SELECT nextval('unique_number_seq') FROM generate_series (1, :length)
            """)
    List<Long> getUniqueNumbers(@Param("length") long length);

    @Query(nativeQuery = true, value = """
            DELETE FROM hash WHERE hash IN (
                SELECT hash FROM hash LIMIT :n
            )RETURNING *
            """)
    List<Hash> findAndDelete(@Param("n") long n);
}
