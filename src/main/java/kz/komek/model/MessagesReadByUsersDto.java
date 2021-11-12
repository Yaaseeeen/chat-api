package kz.komek.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessagesReadByUsersDto extends AbstractDto {

  private Long messagesId;

  private Long readByUserId;

  private Long conversationId;

  private LocalDateTime readTime;

}
