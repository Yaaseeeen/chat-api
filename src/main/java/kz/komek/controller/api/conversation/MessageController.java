package kz.komek.controller.api.conversation;

import kz.komek.model.MessagesDto;
import kz.komek.model.MessagesReadByUsersDto;
import kz.komek.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/message")
public class MessageController {

  private final IMessageService iMessageService;

  @Autowired
  public MessageController(
      IMessageService iMessageService
  ) {
    this.iMessageService = iMessageService;
  }

  /**
   * @param id Получения сообщения по данному идентификатору.
   *
   * Получаем определенное сообщение по идентификатору, в единственном числе.
   */
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessagesDto> getMessageById(@PathVariable("id") Long id) {
    return Optional.ofNullable(iMessageService.getMessage(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param convId По данному ID получаем список всех сообщений в чате.
   *
   * Для получения всех сообщений из определенного чата по идентификатору.
   */
  @GetMapping(path = "/all/{conv_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MessagesDto>> getMessages(@PathVariable("conv_id") Long convId) {
    return Optional.ofNullable(iMessageService.getMessages(convId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param id Удаление сообщения по id
   *
   * Удаляет сообщения из чата.
   */
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> deleteMessage(@PathVariable("id") @NotNull Long id) {
    return Optional.ofNullable(iMessageService.deleteMessage(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param dto Сообщение от чата.
   *
   * Сохранение сообщения от пользователя
   * Сообщение может иметь parent (reply_on_message_id) сообщение в conversation-е
   */
  @RequestMapping(method = {RequestMethod.POST})
  public ResponseEntity<?> saveMessage(@RequestBody @NotNull MessagesDto dto) {
    System.out.println("MessagesDto dto-----------  = " + dto);
    return Optional.ofNullable(iMessageService.saveMessage(dto))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param dto Сохраняет в список прочитанных сообщений
   * @return MessagesReadByUsersDto Возвращает для отобажения на клиентской части.
   *
   * Помечает сообщение в чате как прочитанное.
   */
  @PostMapping(path = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessagesReadByUsersDto> markMessageAsRead(@RequestBody @NotNull MessagesReadByUsersDto dto) {
    return Optional.ofNullable(iMessageService.markMessageAsRead(dto))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param id По данному id производится сброс прочитанного сообщения.
   * @return id Возвращается для потверждения удаления из списка прочитанных сообщений.
   *
   * Помечает сообщение как непрочитанное.
   */
  @PostMapping(path = "/unread/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> unmarkMessageAsRead(@PathVariable("id") @NotNull Long id) {
    return Optional.ofNullable(iMessageService.unmarkMessageAsRead(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param userId Получаем по userId все непрочитанные сообщения пользователя.
   *
   * Получаем каунтер всех непрочитанных сообщений пользователя.
   */
  @GetMapping(path = "/count/unread/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Integer> getCountUnreadMessages(@PathVariable("id") Long userId) {
    return Optional.ofNullable(iMessageService.getCountUnreadMessages(userId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
