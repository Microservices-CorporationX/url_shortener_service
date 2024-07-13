package faang.school.urlshortenerservice.encoder;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Base62Encoder {

    @Value("${custom.base62-characters}")
    private final String base62Characters;

    public List<String> encode(List<Long> numbers){
        return numbers.stream()
                .map(this::generateHash)
                .collect(Collectors.toList());
    }

    private String generateHash(Long number){
        StringBuilder sb = new StringBuilder();
        while (number > 0){
            int remainder = (int) (number % base62Characters.length());
            sb.insert(0, base62Characters.charAt(remainder));
            number = number / 62;
        }
        return sb.toString();
    }
}
