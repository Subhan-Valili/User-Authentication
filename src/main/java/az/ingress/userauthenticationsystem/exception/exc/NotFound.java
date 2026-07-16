package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class NotFound extends BaseException {
    public NotFound(String message) {
        super(ErrorCode.SUBMISSION_NOT_FOUND,message);
    }
}
