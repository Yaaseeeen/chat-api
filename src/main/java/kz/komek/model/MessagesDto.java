package kz.komek.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessagesDto extends AbstractDto {

  private Long id;

  private Long conversationId;

  private Long replyOnMessageId;

  private Long messagedUserId;

  private String message;

  private UserDto userDto;

}
