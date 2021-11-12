package kz.komek.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "properties.security")
@Validated
@Configuration
public class SecurityProperties {

  private boolean enabled;
  private String username;
  private String password;

}
