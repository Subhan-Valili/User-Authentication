package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class OtpSent extends BaseException {
    public OtpSent() {
        super(ErrorCode.OTP_SENT);
    }
}
