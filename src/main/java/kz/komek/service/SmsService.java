package kz.komek.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

  public String send(String phone, String customerName, String verificationCode) {
    try {
      if (Objects.nonNull(phone) && phone.length() == 10) {
        phone = "7" + phone;
      }
      String response = sendSms(phone, verificationCode);
      log.info("Response sms service {} ", response);
      return response;
    } catch (Exception e) {
      throw new RuntimeException("Incorrect data for sending sms");
    }
  }

  /**
   * Отправка SMS
   *
   */
  public String sendSms(String phones, String message) {
    System.out.println("Сообщение отправлено успешно. Ваш код: " + message);
    return message;
  }
}
