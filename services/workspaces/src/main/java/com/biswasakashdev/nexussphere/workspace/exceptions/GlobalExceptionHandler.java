package com.biswasakashdev.nexussphere.workspace.exceptions;


import com.biswasakashdev.nexussphere.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> globalExceptionHandler(Throwable ex) {
        log.error("Exception occurred: ", ex);
        ErrorResponse response = new ErrorResponse("Some internal error occurred.");
        return Mono.just(response);
    }
}
