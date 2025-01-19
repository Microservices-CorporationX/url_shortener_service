package faang.school.urlshortenerservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hash")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hash {
    @Id
    @Column(name = "hash")
    private String hash;

    @Column(name = "unique_number")
    private Long uniqueNumber;

    public Hash(Long uniqueNumber, String hash) {
        this.uniqueNumber = uniqueNumber;
        this.hash = hash;
    }

    @Override
    public String toString() {
        return hash;
    }
}
