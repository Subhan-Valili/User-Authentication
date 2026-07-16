package az.ingress.userauthenticationsystem.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] detailsParams;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailsParams = new Object[0];
    }

    public BaseException(ErrorCode errorCode, Object... detailsParams) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailsParams = copyParams(detailsParams);
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.detailsParams = new Object[0];
    }

    public BaseException(ErrorCode errorCode, Throwable cause, Object... detailsParams) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.detailsParams = copyParams(detailsParams);
    }

    private static Object[] copyParams(Object... detailsParams) {
        if (detailsParams == null || detailsParams.length == 0) {
            return new Object[0];
        }
        return Arrays.copyOf(detailsParams, detailsParams.length);
    }

    public String getFormattedDetails() {
        return errorCode.formatDetails(detailsParams);
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
