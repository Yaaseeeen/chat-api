package kz.komek.service;

import kz.komek.configuration.SmsCodeCheckProps;
import kz.komek.entity.UserEntity;
import kz.komek.model.enums.SmsVerificationState;
import kz.komek.model.enums.UserStatus;
import kz.komek.repository.SmsVerificationRepository;
import kz.komek.service.exceptions.NoSuchSmsException;
import kz.komek.service.exceptions.VerificationCodeExpiredException;
import kz.komek.service.exceptions.WrongVerificationCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsVerificationService {
    private final SmsVerificationRepository verificationRepository;
    private final SmsCodeCheckProps properties;
    private final SmsService smsService;

    @Transactional
    public String requestSmsVerification(@NotNull String phone, String customerName) {

        String verificationCode = generateCode();

        UserEntity entity = verificationRepository.findByPhone(phone);
        String requestSmsVerification = "";
        if (entity == null) {
            entity = new UserEntity();
            entity.setPhone(phone);
        }
        entity.setCreatedAt(LocalDateTime.now());
        entity.setVerificationCode(verificationCode);
        try {
            requestSmsVerification = smsService.send(entity.getPhone(), customerName, verificationCode);
            entity.setStatus(UserStatus.SENT);

        } catch (Exception e) {
            log.error("Error handling {} verification request. {}", phone, e.getMessage());
            entity.setStatus(UserStatus.ERROR);
            requestSmsVerification = "ERROR";
        } finally {
        verificationRepository.save(entity);
        }
        return requestSmsVerification;
    }

    @Transactional
    public void verify(@NotNull String phone, @NotNull String verificationCode) {

        UserEntity verification = verificationRepository.findByPhone(phone);

        if (verification == null) {
            throw new NoSuchSmsException(phone);
        } else {
            if (notExpired(verification)) {
                if (Objects.equals(verificationCode, verification.getVerificationCode())) {
                    log.info("Successfully verified {}. User status is ACTIVE", phone);
                    verification.setStatus(UserStatus.ACTIVE);;
                    verificationRepository.save(verification);
                } else {
                    log.warn("Wrong verification code '{}' for {}", verificationCode, phone);
                    throw new WrongVerificationCodeException(verificationCode);
                }
            } else {
                log.warn("Verification code for {} expired", phone);
                throw new VerificationCodeExpiredException();
            }
        }
    }

    public SmsVerificationState getVerificationState(String phone) {
        UserEntity entity = verificationRepository.findByPhone(phone);
        return new SmsVerificationState(entity.getPhone(), entity.getStatus());
    }

    private boolean notExpired(UserEntity verification) {
        Integer validityPeriodInHours = properties.getValidityPeriod();
        return verification.getCreatedAt()
                .plusHours(validityPeriodInHours)
                .isAfter(LocalDateTime.now());
    }

    private String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
    }
}
