package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class UserAlreadyExists extends BaseException {
    public UserAlreadyExists() {
        super(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
    }
}
