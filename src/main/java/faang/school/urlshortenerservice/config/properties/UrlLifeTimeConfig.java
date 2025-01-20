package faang.school.urlshortenerservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.url.life-time")
public class UrlLifeTimeConfig {
    private int months;
    private int days;
    private int hours;
}
