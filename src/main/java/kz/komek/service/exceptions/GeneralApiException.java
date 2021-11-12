package kz.komek.service.exceptions;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GeneralApiException extends RuntimeException {

  @JsonProperty
  private ApiErrorType errorType;

  @JsonProperty
  private Map<ApiErrorParam, ? extends Object> errorParameters;

  public GeneralApiException(ApiErrorType type) {
    super();
    this.errorType = type;
  }

  public GeneralApiException(ApiErrorType type,
                             Map<ApiErrorParam, ? extends Object> errorParameters, String message) {
    super(message);
    this.errorType = type;
    this.errorParameters = errorParameters;
  }

  public GeneralApiException(ApiErrorType type,
                             Map<ApiErrorParam, ? extends Object> errorParameters) {
    super();
    this.errorType = type;
    this.errorParameters = errorParameters;
  }

  @JsonProperty
  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
