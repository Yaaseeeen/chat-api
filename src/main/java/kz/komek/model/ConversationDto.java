package kz.komek.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConversationDto extends AbstractDto {

  private Long id;

  private String title;

  private Long createdUserId;

  private boolean isPrivate;

  private Integer countUnreadMessage;

  private UserDto createdUser;

}
