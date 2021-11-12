package kz.komek.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class WatchingUsersDto extends AbstractDto {

  private Long id;

  private Long conversationId;

  private Long userId;

  private UserDto userDto;

}
