package az.ingress.userauthenticationsystem.exception;

import java.time.Instant;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.builder()
                .status(ex.getHttpStatus().value())
                .code(ex.getErrorCode().getCode())
                .message(ex.getFormattedDetails())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse body = ErrorResponse.builder()
                .status(code.getHttpStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseValid> handleValidation(MethodArgumentNotValidException ex,
                                                               HttpServletRequest request) {
        ErrorCode code = ErrorCode.VALIDATION_FAILED;
        List<ValidationResponse> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ValidationResponse.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();

        ErrorResponseValid body = ErrorResponseValid.builder()
                .status(code.getHttpStatus().value())
                .code(code.getCode())
                .message(code.getMessage())
                .timestamp(Instant.now())
                .path(request.getRequestURI()) // DƏYİŞİKLİK BURADADIR: Birbaşa URI-ni alırıq
                .validations(fieldErrors)
                .build();

        return ResponseEntity.status(code.getHttpStatus()).body(body);
    }
}