package org.bro.banking.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.bro.banking.domain.exception.CustomExcepting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomExcepting.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomExcepting ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorType", ex.getErrorType());
        body.put("message", ex.getMessage());
        body.put("status", ex.getStatus().value());

        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

        log.debug("Validation exception occurred: {}", ex.getMessage());


        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    //    @ExceptionHandler(Exception.class)
    //    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
    //        Map<String, Object> body = new HashMap<>();
    //        body.put("message", "An unexpected error occurred");
    //        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    //
    //        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    //    }
}
