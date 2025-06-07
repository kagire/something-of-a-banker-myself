package bank.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(HttpServletRequest request, Exception ex) {
        log.error("Exception caught at URI: {}", request.getRequestURI());
        log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());

        ex.printStackTrace(); //<-- I know it's not cool, just for debug

        return switch (ex) {
            case ConstraintViolationException cex ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(cex.getConstraintViolations().stream()
                        .findFirst().orElseThrow().getMessage());
            case IllegalStateException iex ->
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(iex.getMessage());
            case IllegalArgumentException iex ->
                ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(iex.getMessage());
            case BadCredentialsException bex ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(bex.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + ex.getMessage());
        };
    }
}
