package faang.school.urlshortenerservice.exception.handler;

import faang.school.urlshortenerservice.exception.DataValidationException;
import faang.school.urlshortenerservice.exception.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataValidationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(Exception e) {
        logException(e, LogLevel.WARN);
        return buildResponse(e);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(EntityNotFoundException e) {
        logException(e, LogLevel.WARN);
        return buildResponse(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerExceptions(Exception e) {
        logException(e, LogLevel.ERROR);
        return ErrorResponse.builder()
                .message("Internal server error!")
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ErrorResponse buildResponse(Exception e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void logException(Exception e, LogLevel logLevel) {
        String logMessage = "%s: %s".formatted(e.getClass().getSimpleName(), e.getMessage());
        switch (logLevel) {
            case ERROR -> log.error(logMessage, e);
            case WARN -> log.warn(logMessage, e);
        }
    }

    private enum LogLevel {
        ERROR, WARN
    }
}
