package kz.komek.controller.api.conversation;

import kz.komek.model.SmsVerification;
import kz.komek.model.VerificationRequest;
import kz.komek.service.SmsVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/sms")
public class SmsVerificationController {

    private final SmsVerificationService verificationService;

    @PostMapping(path = "/send")
    @ResponseStatus(HttpStatus.CREATED)
    public String requestSmsVerification(@Validated @RequestBody VerificationRequest verificationRequest) {
        log.info("New verification for {} {}", verificationRequest.getName(), verificationRequest.getPhone());
        return verificationService.requestSmsVerification(verificationRequest.getPhone(), verificationRequest.getName());
    }

    @PostMapping(path = "/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(
        @Validated
        @RequestBody SmsVerification verification
    ) {
        log.info("Verify {} with code '{}'", verification.getPhone(), verification.getVerificationCode());
        verificationService.verify(verification.getPhone(), verification.getVerificationCode());
    }
}
