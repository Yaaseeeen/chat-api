package kz.komek.component.converter;

import javax.annotation.PostConstruct;
import kz.komek.entity.MessagesReadByUsersEntity;
import kz.komek.model.MessagesReadByUsersDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageReadUserMapper
    extends AbstractMapper<MessagesReadByUsersEntity, MessagesReadByUsersDto> {

  private final ModelMapper mapper;

  MessageReadUserMapper(ModelMapper mapper) {
    super(MessagesReadByUsersEntity.class, MessagesReadByUsersDto.class);
    this.mapper = mapper;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(MessagesReadByUsersEntity.class, MessagesReadByUsersDto.class)
        .setPostConverter(toDtoConverter());
    mapper.createTypeMap(MessagesReadByUsersDto.class, MessagesReadByUsersEntity.class)
        .setPostConverter(toEntityConverter());
  }

}
