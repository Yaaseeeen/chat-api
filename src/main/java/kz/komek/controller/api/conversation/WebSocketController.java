package kz.komek.controller.api.conversation;

import kz.komek.model.ConversationDto;
import kz.komek.model.MessagesDto;
import kz.komek.service.IConversationService;
import kz.komek.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Controller
public class WebSocketController {

  private final IMessageService iMessageService;
  private final IConversationService iConversationService;

  @Autowired
  public WebSocketController(
      IMessageService iMessageService,
      IConversationService iConversationService
  ) {
    this.iMessageService = iMessageService;
    this.iConversationService = iConversationService;
  }

  @MessageMapping("/message/save")
  @SendTo("/topic/websocket")
  public ResponseEntity<MessagesDto> saveMessage(@RequestBody @NotNull MessagesDto dto) {
    MessagesDto messagesDto = iMessageService.saveMessage(dto);

    return Optional.ofNullable(messagesDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param dto     Обьект для создания чата.
   * @return Возвращяем обьект созданного чата.
   *
   */
  @MessageMapping("/conversation/save/")
  @SendTo("/topic/conversation")
  public ResponseEntity<ConversationDto> createConversation(@RequestBody @NotNull ConversationDto dto) {
    return Optional.ofNullable(iConversationService.createConversation(dto))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
