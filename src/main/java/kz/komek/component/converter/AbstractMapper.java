package kz.komek.component.converter;

import java.util.Objects;
import kz.komek.entity.AbstractEntity;
import kz.komek.model.AbstractDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMapper<E extends AbstractEntity, D extends AbstractDto>
    implements Mapper<E, D> {

  private final Class<E> entityClass;
  private final Class<D> dtoClass;
  @Autowired
  private ModelMapper mapper;

  AbstractMapper(Class<E> entityClass, Class<D> dtoClass) {
    this.entityClass = entityClass;
    this.dtoClass = dtoClass;
  }

  @Override
  public E toEntity(D dto) {
    return Objects.isNull(dto) ? null : mapper.map(dto, entityClass);
  }

  @Override
  public D toDto(E entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, dtoClass);
  }

  Converter<E, D> toDtoConverter() {
    return context -> {
      E source = context.getSource();
      D destination = context.getDestination();
      mapSpecificFields(source, destination);
      return context.getDestination();
    };
  }

  Converter<D, E> toEntityConverter() {
    return context -> {
      D source = context.getSource();
      E destination = context.getDestination();
      mapSpecificFields(source, destination);
      return context.getDestination();
    };
  }

  void mapSpecificFields(E source, D destination) {
  }

  void mapSpecificFields(D source, E destination) {
  }

}
