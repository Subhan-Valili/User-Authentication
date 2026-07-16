package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class SessionExpired extends BaseException {
    public SessionExpired() {
        super(ErrorCode.REGISTRATION_SESSION_EXPIRED);
    }
}
