package kz.komek.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "sms-code-check-api")
public class SmsCodeCheckProps {

    @NotNull
    private Integer validityPeriod = 24;

    private Integer codeLength = 6;

    private String login;

    private String password;

    private String projectName;
}
