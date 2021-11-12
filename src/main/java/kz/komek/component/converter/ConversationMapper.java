package kz.komek.component.converter;

import javax.annotation.PostConstruct;
import kz.komek.entity.ConversationEntity;
import kz.komek.model.ConversationDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConversationMapper extends AbstractMapper<ConversationEntity, ConversationDto> {

  private final ModelMapper mapper;

  ConversationMapper(ModelMapper mapper) {
    super(ConversationEntity.class, ConversationDto.class);
    this.mapper = mapper;
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(ConversationEntity.class, ConversationDto.class)
        .setPostConverter(toDtoConverter());
    mapper.createTypeMap(ConversationDto.class, ConversationEntity.class)
        .setPostConverter(toEntityConverter());
  }
}
