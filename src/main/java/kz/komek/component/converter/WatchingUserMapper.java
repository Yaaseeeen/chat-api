package kz.komek.component.converter;

import javax.annotation.PostConstruct;
import kz.komek.entity.UserEntity;
import kz.komek.entity.WatchingUsersEntity;
import kz.komek.model.UserDto;
import kz.komek.model.WatchingUsersDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WatchingUserMapper
    extends AbstractMapper<WatchingUsersEntity, WatchingUsersDto> {

  private final ModelMapper mapper;

  WatchingUserMapper(ModelMapper mapper) {
    super(WatchingUsersEntity.class, WatchingUsersDto.class);
    this.mapper = mapper;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(WatchingUsersEntity.class, WatchingUsersDto.class)
        .setPostConverter(toDtoConverter());
    mapper.createTypeMap(WatchingUsersDto.class, WatchingUsersEntity.class)
        .setPostConverter(toEntityConverter());
  }

  public WatchingUsersDto specificMap(WatchingUsersEntity watchingUsersEntity){
    WatchingUsersDto watchingUsersDto = mapper.map(watchingUsersEntity, WatchingUsersDto.class);

    watchingUsersDto.setUserDto(mapper.map(watchingUsersEntity.getUserEntity(), UserDto.class));

    return watchingUsersDto;
  }

}
