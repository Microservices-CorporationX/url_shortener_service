package faang.school.urlshortenerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Builder
public record UrlDto(
        @NotNull(message = "URL can not be null")
        @Pattern(regexp = "^(http|https)://.*$", message = "URL must start with http or https")
        String url
) {

}
