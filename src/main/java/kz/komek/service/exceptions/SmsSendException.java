package kz.komek.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to send sms")
public class SmsSendException extends RuntimeException {
    public SmsSendException(String message) {
        super(message);
    }
}
