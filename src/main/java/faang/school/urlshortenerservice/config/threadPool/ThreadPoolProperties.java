package faang.school.urlshortenerservice.config.threadPool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("thread-pool")
public class ThreadPoolProperties {
    private Integer size;
    private Integer maxSize;
    private Integer timeout;
}
