package ru.corporationx.urlshortenerservice.encoder;

import java.util.List;

public interface BaseEncoder {

    List<String> encode(List<Long> numbers);
}
