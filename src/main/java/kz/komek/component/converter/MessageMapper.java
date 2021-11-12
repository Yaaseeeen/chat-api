package kz.komek.component.converter;

import kz.komek.entity.MessagesEntity;
import kz.komek.model.MessagesDto;
import kz.komek.model.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class MessageMapper extends AbstractMapper<MessagesEntity, MessagesDto> {

  private final ModelMapper mapper;

  MessageMapper(ModelMapper mapper) {
    super(MessagesEntity.class, MessagesDto.class);
    this.mapper = mapper;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(MessagesEntity.class, MessagesDto.class)
        .setPostConverter(toDtoConverter());
    mapper.createTypeMap(MessagesDto.class, MessagesEntity.class)
        .setPostConverter(toEntityConverter());
  }

  public MessagesDto specificMap(MessagesEntity messagesEntity){
    MessagesDto messagesDto = mapper.map(messagesEntity, MessagesDto.class);
    if(Objects.nonNull(messagesEntity.getUserEntity())) {
      messagesDto.setUserDto(
          mapper.map(messagesEntity.getUserEntity(), UserDto.class)
      );
    }
    return messagesDto;
  }

}
