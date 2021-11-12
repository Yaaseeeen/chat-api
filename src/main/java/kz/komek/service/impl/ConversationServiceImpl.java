package kz.komek.service.impl;

import kz.komek.component.converter.ConversationMapper;
import kz.komek.component.converter.MessageMapper;
import kz.komek.component.converter.WatchingUserMapper;
import kz.komek.entity.ConversationEntity;
import kz.komek.entity.MessagesEntity;
import kz.komek.entity.WatchingUsersEntity;
import kz.komek.model.ConversationDto;
import kz.komek.model.MessagesDto;
import kz.komek.model.WatchingUsersDto;
import kz.komek.model.other.ConversationInfo;
import kz.komek.repository.ConversationRepository;
import kz.komek.repository.MessageRepository;
import kz.komek.repository.MessagesReadUserRepository;
import kz.komek.repository.WatchingUserRepository;
import kz.komek.service.IConversationService;
import kz.komek.service.exceptions.ApiErrorType;
import kz.komek.service.exceptions.GeneralApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConversationServiceImpl implements IConversationService {

  private final MessageRepository messageRepository;
  private final MessageMapper messageMapper;
  private final ConversationMapper convMapper;
  private final ConversationRepository convRepository;
  private final MessagesReadUserRepository messageReadUserRepository;
  private final WatchingUserRepository watchingUserRepository;
  private final WatchingUserMapper watchingUserMapper;

  /**
   * ConversationServiceImpl constructor.
   *
   * @param messageRepository         {@link MessageRepository}
   * @param messageMapper             {@link MessageMapper}
   * @param convMapper                {@link ConversationMapper}
   * @param convRepository            {@link ConversationRepository}
   * @param watchingUserRepository    {@link WatchingUserRepository}
   * @param watchingUserMapper        {@link WatchingUserMapper}
   * @param messageReadUserRepository {@link MessagesReadUserRepository}
   */
  @Autowired
  public ConversationServiceImpl(
      MessageRepository messageRepository,
      MessageMapper messageMapper,
      ConversationMapper convMapper,
      ConversationRepository convRepository,
      WatchingUserRepository watchingUserRepository,
      WatchingUserMapper watchingUserMapper,
      MessagesReadUserRepository messageReadUserRepository
  ) {
    this.messageRepository = messageRepository;
    this.messageMapper = messageMapper;
    this.convMapper = convMapper;
    this.convRepository = convRepository;
    this.messageReadUserRepository = messageReadUserRepository;
    this.watchingUserRepository = watchingUserRepository;
    this.watchingUserMapper = watchingUserMapper;
  }

  @Override
  public Long deleteConversation(Long id) {
    ConversationEntity entity = convRepository.findById(id)
                .orElseThrow(() -> new GeneralApiException(ApiErrorType.E500_CONVERSATION_NOT_FOUND));
    convRepository.save(entity);
    return id;
  }

  @Override
  public List<ConversationDto> getConversationByUserId(Long userId) {
    List<WatchingUsersEntity> entities =
        watchingUserRepository.findAllByUserId(userId);

    List<ConversationEntity> list = entities.stream().map(WatchingUsersEntity::getConversationEntity)
        .collect(Collectors.toList());

    List<ConversationDto> result = list.stream().map(convMapper::toDto).collect(Collectors.toList());
    if(result.isEmpty()) return result;

    List<Long> conversationIds = result.stream().map(ConversationDto::getId).collect(Collectors.toList());

    watchingUserRepository.countMessages(conversationIds).forEach(messageCount -> {
      result.forEach(item -> {
        Integer integer = (Integer) messageCount.get("conversation_id");
        Integer count = (Integer) messageCount.get("count");
        if (Objects.equals(item.getId(), integer.longValue())) {
          item.setCountUnreadMessage(count);
        }
      });
    });

    watchingUserRepository.readCountMessages(userId, conversationIds).forEach(messageReadCount -> {
      result.forEach(item -> {
        Integer integer = (Integer) messageReadCount.get("conversation_id");
        Integer count = (Integer) messageReadCount.get("count");
        if (Objects.equals(item.getId(), integer.longValue())) {
          item.setCountUnreadMessage(item.getCountUnreadMessage() - count);
        }
      });
    });
    return result;
  }

  @Override
  public List<ConversationDto> getUnreadConversationByUserId(Long userId) {
    List<ConversationDto> list = getConversationByUserId(userId);
    return list.stream().filter(item -> Objects.nonNull(item.getCountUnreadMessage()) && item.getCountUnreadMessage() > 0)
            .collect(Collectors.toList());
  }

  @Override
  public ConversationDto getConversation(Long id) {
    Optional<ConversationEntity> entity = convRepository.findById(id);
    return entity.map(convMapper::toDto)
        .orElseThrow(() -> new GeneralApiException(ApiErrorType.E500_CONVERSATION_NOT_FOUND));
  }

  @Override
  public List<WatchingUsersDto> getListConversationWatchers(Long convId) {
    List<WatchingUsersEntity> entities = watchingUserRepository.
        findAllByConversationId(convId);

    return entities.stream().map(watchingUserMapper::specificMap).collect(Collectors.toList());
  }

  @Override
  public ConversationDto createConversation(ConversationDto dto) {
    ConversationEntity entity = convMapper.toEntity(dto);
    entity = convRepository.save(entity);

    WatchingUsersDto watchingUsersDto = WatchingUsersDto.builder()
        .userId(dto.getCreatedUserId())
        .conversationId(entity.getId())
        .build();

    WatchingUsersEntity watchingEntity = watchingUserMapper.toEntity(watchingUsersDto);
    watchingUserRepository.save(watchingEntity);
    return convMapper.toDto(entity);
  }

  @Override
  public Integer markConversationMessagesAsRead(Long conversationId, Long userId) {
    return messageReadUserRepository.readAll(conversationId, userId, LocalDateTime.now());
  }

  @Override
  public ConversationInfo getConversationInfo(Long convId) {
    List<MessagesEntity> entities = messageRepository.findAllByConversationId(convId);

    List<MessagesDto> messagesDtos = entities.stream()
        .map(messageMapper::specificMap).collect(Collectors.toList());


    List<WatchingUsersDto> watchingUsersDtos = watchingUserRepository
        .findAllByConversationId(convId).stream()
        .map(watchingUserMapper::specificMap)
        .collect(Collectors.toList());

    ConversationInfo conversationInfo = ConversationInfo.builder()
        .messages(messagesDtos)
        .watchingUsers(watchingUsersDtos)
        .build();
    return conversationInfo;
  }

  @Override
  public WatchingUsersDto saveUserWatchingConversation(WatchingUsersDto dto) {
    Optional<ConversationEntity> convEntity =
        this.convRepository.findById(dto.getConversationId());
    if (!convEntity.isPresent()) {
      throw new GeneralApiException(ApiErrorType.E500_CONVERSATION_NOT_FOUND);
    }
    WatchingUsersEntity watchingUsersEntity = watchingUserRepository
        .findAllByConversationIdAndUserId(dto.getConversationId(), dto.getUserId());
    if (Objects.nonNull(watchingUsersEntity)) {
      throw new GeneralApiException(ApiErrorType.E500_WATCHER_DUPLICATED);
    }
    WatchingUsersEntity entity = watchingUserMapper.toEntity(dto);
    entity = watchingUserRepository.save(entity);
    return watchingUserMapper.toDto(entity);
  }

  @Override
  public Long deleteUserWatchingConversation(Long id) {
    Optional<WatchingUsersEntity> entity = watchingUserRepository.findById(id);
    if (!entity.isPresent()) {
      throw new GeneralApiException(ApiErrorType.E500_WATCHER_NOT_FOUND);
    }
    watchingUserRepository.deleteById(id);
    return id;
  }
}
