package faang.school.urlshortenerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDto {
    private String hash;
    @NotBlank
    private String url;
    private LocalDateTime createdAt;
}
