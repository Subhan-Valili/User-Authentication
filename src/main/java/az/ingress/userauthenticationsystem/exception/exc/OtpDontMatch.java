package az.ingress.userauthenticationsystem.exception.exc;

import az.ingress.userauthenticationsystem.exception.BaseException;
import az.ingress.userauthenticationsystem.exception.ErrorCode;

public class OtpDontMatch extends BaseException {
    public OtpDontMatch(int message) {
        super(ErrorCode.OTP_MISMATCH, message);
    }
}
