package kz.komek.model.other;

import kz.komek.model.MessagesDto;
import kz.komek.model.WatchingUsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ConversationInfo {

  private List<MessagesDto> messages;
  private List<WatchingUsersDto> watchingUsers;
}
