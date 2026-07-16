package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class OtpExpired extends BaseException {
    public OtpExpired() {
        super(ErrorCode.OTP_EXPIRED);
    }
}
