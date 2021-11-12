package kz.komek.repository;

import java.util.List;
import kz.komek.entity.MessagesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends
    CrudRepository<MessagesEntity, Long> {

  List<MessagesEntity> findAllByConversationId(Long conversationId);

  @Query("select COUNT(o) from MessagesEntity o where conversation_id in :ids")
  Integer countByConversationIds(@Param("ids") List<Long> ids);

}
