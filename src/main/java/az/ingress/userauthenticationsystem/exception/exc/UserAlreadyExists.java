package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class UserAlreadyExists extends BaseException {
    public UserAlreadyExists(String message) {
        super(ErrorCode.USER_ALREADY_EXISTS,message);
    }
}
