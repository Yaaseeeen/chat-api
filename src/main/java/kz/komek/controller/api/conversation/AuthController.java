package kz.komek.controller.api.conversation;

import kz.komek.model.UserDto;
import kz.komek.service.IAuthService;
import kz.komek.service.IConversationService;
import kz.komek.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/user")
public class AuthController {

  private final IMessageService iMessageService;
  private final IConversationService iConversationService;
  private final IAuthService iUserService;

  @Autowired
  public AuthController(
      IMessageService iMessageService,
      IConversationService iConversationService,
      IAuthService iUserService
  ) {
    this.iMessageService = iMessageService;
    this.iConversationService = iConversationService;
    this.iUserService = iUserService;
  }

  /**
   *
   * Получаем всех пользователей.
   */

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDto>> getUsers(@PathVariable("id") @NotNull Long convId) {
    return Optional.ofNullable(iUserService.getUsers(convId))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
