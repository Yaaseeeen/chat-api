package kz.komek.service.impl;

import kz.komek.component.converter.MessageMapper;
import kz.komek.component.converter.MessageReadUserMapper;
import kz.komek.component.converter.WatchingUserMapper;
import kz.komek.entity.*;
import kz.komek.model.MessagesDto;
import kz.komek.model.MessagesReadByUsersDto;
import kz.komek.repository.*;
import kz.komek.service.IMessageService;
import kz.komek.service.exceptions.ApiErrorType;
import kz.komek.service.exceptions.GeneralApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {

  private final MessageRepository messageRepository;
  private final ConversationRepository conversationRepository;
  private final MessageMapper messageMapper;
  private final WatchingUserRepository watchingUserRepository;
  private final WatchingUserMapper watchingUserMapper;
  private final MessageReadUserMapper readUserMapper;
  private final MessagesReadUserRepository messageReadUserRepository;
  private final UserRepository userRepository;

  /**
   * MessageServiceImpl constructor.
   *  @param messageRepository            {@link MessageMapper}
   * @param conversationRepository       {@link ConversationRepository}
   * @param messageMapper                {@link MessageRepository}
   * @param watchingUserRepository       {@link WatchingUserRepository}
   * @param watchingUserMapper           {@link WatchingUserMapper}
   * @param readUserMapper               {@link MessageReadUserMapper}
   * @param messageReadUserRepository    {@link MessagesReadUserRepository}
   * @param userRepository               {@link UserRepository}
   */
  @Autowired
  public MessageServiceImpl(
          MessageRepository messageRepository,
          ConversationRepository conversationRepository,
          MessageMapper messageMapper,
          WatchingUserRepository watchingUserRepository,
          WatchingUserMapper watchingUserMapper,
          MessageReadUserMapper readUserMapper,
          MessagesReadUserRepository messageReadUserRepository,
          UserRepository userRepository
  ) {
    this.messageRepository = messageRepository;
    this.conversationRepository = conversationRepository;
    this.messageMapper = messageMapper;
    this.watchingUserRepository = watchingUserRepository;
    this.watchingUserMapper = watchingUserMapper;
    this.readUserMapper = readUserMapper;
    this.messageReadUserRepository = messageReadUserRepository;
    this.userRepository = userRepository;
  }


  @Override
  public Long deleteMessage(Long id) {
    Optional<MessagesEntity> entity = messageRepository.findById(id);
    if (!entity.isPresent()) {
      throw new GeneralApiException(ApiErrorType.E500_MESSAGE_NOT_FOUND);
    }
    messageRepository.deleteById(id);
    return id;
  }

  @Override
  public Integer getCountUnreadMessages(Long userId) {
    List<WatchingUsersEntity> entities =
        watchingUserRepository.findAllByUserId(userId);

    List<Long> ids = entities.stream().map(WatchingUsersEntity::getConversationId)
        .collect(Collectors.toList());

    if (ids.isEmpty()) return 0;

    Integer countMessage = messageRepository.countByConversationIds(ids);

    Integer readMessage = messageReadUserRepository.findAllByReadByUserId(userId, ids);

    int result = countMessage - readMessage;

    return Math.max(result, 0);
  }

  @Override
  public MessagesDto getMessage(Long id) {
    Optional<MessagesEntity> entity = messageRepository.findById(id);
    return entity.map(messageMapper::toDto)
        .orElseThrow(() -> new GeneralApiException(ApiErrorType.E500_MESSAGE_NOT_FOUND));
  }

  @Override
  public List<MessagesDto> getMessages(Long convId) {
    List<MessagesEntity> listEntity = messageRepository.findAllByConversationId(convId);
    return listEntity.stream()
        .map(messageMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public MessagesDto saveMessage(MessagesDto dto) {
    MessagesEntity entity = messageMapper.toEntity(dto);
    if (Objects.nonNull(dto)) {
      entity = messageRepository.save(entity);
      Optional<ConversationEntity> conversation = conversationRepository.findById(entity.getConversationId());

      conversation.ifPresent(conversationEntity -> {
        conversationEntity.toUpdate();
        conversationRepository.save(conversationEntity);
      });
      MessagesReadByUsersEntity readEntity = MessagesReadByUsersEntity.builder()
          .readByUserId(dto.getMessagedUserId())
          .conversationId(dto.getConversationId())
          .messagesId(entity.getId())
          .build();

      messageReadUserRepository.save(readEntity);

      Optional<UserEntity> userEntity = userRepository
          .findById(dto.getMessagedUserId());

      if (userEntity.isPresent()) {
        entity.setUserEntity(userEntity.get());
      }

      return messageMapper.specificMap(entity);
    }

    return messageMapper.toDto(entity);
  }

  @Override
  public MessagesReadByUsersDto markMessageAsRead(MessagesReadByUsersDto dto) {
    Optional<MessagesEntity> message =
        this.messageRepository.findById(dto.getMessagesId());

    if (!message.isPresent()) {
      throw new GeneralApiException(ApiErrorType.E500_MESSAGE_NOT_FOUND);
    }

    MessagesReadByUsersEntity entity = readUserMapper.toEntity(dto);

    messageReadUserRepository.save(entity);

    return readUserMapper.toDto(entity);
  }

  @Override
  public Long unmarkMessageAsRead(Long id) {
    Optional<MessagesReadByUsersEntity> message =
        this.messageReadUserRepository.findById(id);
    if (!message.isPresent()) {
      throw new GeneralApiException(ApiErrorType.E500_READ_MESSAGE_NOT_FOUND);
    }
    this.messageReadUserRepository.deleteById(id);
    return id;
  }
}
