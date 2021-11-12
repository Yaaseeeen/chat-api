package kz.komek.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "storage.properties")
@Validated
@Configuration
public class StorageProperties {

	private String location;

}
