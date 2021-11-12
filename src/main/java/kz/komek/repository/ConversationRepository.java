package kz.komek.repository;

import kz.komek.entity.ConversationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends
    CrudRepository<ConversationEntity, Long> {
}
