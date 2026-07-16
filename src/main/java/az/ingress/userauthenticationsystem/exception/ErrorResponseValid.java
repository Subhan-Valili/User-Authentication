package az.ingress.userauthenticationsystem.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseValid {

    private Integer status;
    private String code;
    private String message;
    private Instant timestamp;
    private String path;
    private List<ValidationResponse> validations;
}
