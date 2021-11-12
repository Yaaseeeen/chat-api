package kz.komek.service.impl;

import kz.komek.entity.UserEntity;
import kz.komek.entity.WatchingUsersEntity;
import kz.komek.model.UserDto;
import kz.komek.repository.UserRepository;
import kz.komek.repository.WatchingUserRepository;
import kz.komek.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class IAuthServiceImpl implements IAuthService {

  private final WatchingUserRepository watchingUserRepository;
  private final UserRepository userRepository;
  private final ModelMapper mapper;

  /**
   * IUserServiceImpl constructor.
   *
   * @param watchingUserRepository       {@link WatchingUserRepository}
   * @param userRepository               {@link UserRepository}
   */

  @Autowired
  public IAuthServiceImpl(
    WatchingUserRepository watchingUserRepository,
    UserRepository userRepository,
    ModelMapper mapper
  ) {
    this.watchingUserRepository = watchingUserRepository;
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  @Override
  public List<UserDto> getUsers(Long convId) {
    Iterable<UserEntity> userEntities = userRepository.findAll();

    List<UserEntity> entities = StreamSupport.stream(userEntities.spliterator(), false)
      .collect(Collectors.toList());

    List<Long> ids = watchingUserRepository.findAllByConversationId(convId).stream()
      .map(WatchingUsersEntity::getUserId).collect(Collectors.toList());

    List<UserEntity> resultEntity = entities.stream()
      .filter(item -> !ids.contains(item.getId())).collect(Collectors.toList());

    return resultEntity.stream().map(item -> mapper.map(item, UserDto.class))
      .collect(Collectors.toList());
  }
}
