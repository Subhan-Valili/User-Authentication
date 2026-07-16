package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class RoleNotFound extends BaseException {

    public RoleNotFound(String message) {
        super(ErrorCode.ROLE_NOT_FOUND, message);
    }

}
