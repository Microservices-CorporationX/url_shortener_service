package faang.school.urlshortenerservice.config.executor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "executor")
public class ExecutorParams {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
}
