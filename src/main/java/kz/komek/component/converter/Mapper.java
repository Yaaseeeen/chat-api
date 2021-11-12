package kz.komek.component.converter;

import kz.komek.entity.AbstractEntity;
import kz.komek.model.AbstractDto;


public interface Mapper<E extends AbstractEntity, D extends AbstractDto> {

  E toEntity(D dto);

  D toDto(E entity);

}
