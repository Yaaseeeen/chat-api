package kz.komek.controller.api.conversation;

import kz.komek.entity.UserEntity;
import kz.komek.model.ConversationDto;
import kz.komek.model.WatchingUsersDto;
import kz.komek.model.enums.UserStatus;
import kz.komek.model.other.ConversationInfo;
import kz.komek.repository.UserRepository;
import kz.komek.service.IConversationService;
import kz.komek.service.exceptions.NoSuchUserException;
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
@RequestMapping(path = "/conversation")
public class ConversationController {

  private final IConversationService iConversationService;
  private final UserRepository userRepository;

  @Autowired
  public ConversationController(
          IConversationService iConversationService,
          UserRepository userRepository) {
    this.iConversationService = iConversationService;
    this.userRepository = userRepository;
  }

  /**
   * @param id Получить чат по id
   * @return возвращаем данные чата в единственном числе.
   */
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ConversationDto> getConversation(@PathVariable("id") Long id) {
    return Optional.ofNullable(iConversationService.getConversation(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param userId Получить чат по user_id
   * @return возвращаем список чатов
   */
  @GetMapping(path = "/user/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ConversationDto>> getConversationByUserId(@PathVariable("user_id") Long userId) {
    return Optional.ofNullable(iConversationService.getConversationByUserId(userId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param userId Получить чат по user_id
   * @return возвращаем список чатов
   */
  @GetMapping(path = "unread/user/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ConversationDto>> getUnreadConversationByUserId(@PathVariable("user_id") Long userId) {
    return Optional.ofNullable(iConversationService.getUnreadConversationByUserId(userId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param dto     Обьект для создания чата.
   * @return Возвращяем обьект созданного чата.
   *
   */
  @PostMapping()
  public ResponseEntity<?> createConversation(@RequestBody @NotNull ConversationDto dto) {
    Optional<UserEntity> optionalUserEntity = userRepository.findById(dto.getCreatedUserId());
    if (optionalUserEntity.isPresent()
            && optionalUserEntity.get().getStatus().equals(UserStatus.ACTIVE)){
     return Optional.ofNullable(iConversationService.createConversation(dto))
             .map(ResponseEntity::ok)
             .orElse(ResponseEntity.notFound().build());   }
    throw new NoSuchUserException("У вас нет доступа");
  }

  /**
   * @param id Удаляем чат по идентификатору.
   * @return Возвращяем id удаленного чата.
   *
   * Для удаления чата.
   */
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> deleteConversation(@PathVariable("id") @NotNull Long id) {
    return Optional.ofNullable(iConversationService.deleteConversation(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param convId Получаем сообщение по данному идентификатору.
   * @return Возвращяется список наблюдающих пользователей за чатом.
   *
   * Получить список наблюдающих пользователей за чатом.
   */
  @GetMapping(path = "/{id}/watchers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<WatchingUsersDto>> getListConversationWatchers(@PathVariable("id") Long convId) {
    return Optional.ofNullable(iConversationService.getListConversationWatchers(convId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param id Идентификатор сообщения.
   * @param userId Идентификатор пользователя
   * @return MessagesWatchingUsersDto Возвращяем непрочитанное сообщение.
   *
   * Помечаем все сообщения в чате как прочитанные.
   */
  @PostMapping(path = "{id}/read/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> markConversationMessagesAsRead(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
    return Optional.ofNullable(iConversationService.markConversationMessagesAsRead(id, userId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param dto Создает наблюдения пользователя
   *
   * Добавляет наблюдающего пользователя к чату
   */
  @PostMapping(path = "/watcher", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> saveUserWatchingConversation(@RequestBody @NotNull WatchingUsersDto dto) {
    return Optional.ofNullable(iConversationService.saveUserWatchingConversation(dto))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * @param id Удаляет пользователя по id от наблюдения.
   *
   * Удаляет наблюдающего пользователя от сообщений
   */
  @DeleteMapping(path = "/watcher/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> deleteUserWatchingConversation(@PathVariable("id") @NotNull Long id) {
    return Optional.ofNullable(iConversationService.deleteUserWatchingConversation(id))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping(path = "/info/{convId}")
  public ResponseEntity<ConversationInfo> getConversationInfo(@PathVariable("convId") @NotNull Long convId) {
    ConversationInfo info = iConversationService.getConversationInfo(convId);

    return Optional.ofNullable(info)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
