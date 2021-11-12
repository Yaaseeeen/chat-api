package kz.komek.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Wrong verification code")
public class WrongVerificationCodeException extends RuntimeException {
    private String code;
}
