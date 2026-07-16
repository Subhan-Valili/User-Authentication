package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class EmailExists extends BaseException {
    public  EmailExists(String message) {
        super(ErrorCode.USER_ALREADY_EXISTS,message);
    }
}
