package kz.komek.repository;

import kz.komek.entity.MessagesReadByUsersEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessagesReadUserRepository extends
    CrudRepository<MessagesReadByUsersEntity, Long> {


  @Query("SELECT COUNT(m) FROM MessagesReadByUsersEntity m WHERE read_by_user_id = :userId " +
      "and conversation_id in :ids")
  Integer findAllByReadByUserId(@Param("userId") Long userId, @Param("ids") List<Long> ids);

  @Transactional
  @Modifying
  @Query(value = "INSERT INTO chat_example.messages_read_by_users " +
      "(read_by_user_id, messages_id, conversation_id, date_created, date_updated) " +
      "(select :userId, m.id, m.conversation_id, :localDateTime, :localDateTime  " +
      "FROM chat_example.messages as m where m.conversation_id = :conversationId) " +
      "ON CONFLICT DO NOTHING;", nativeQuery = true)
  Integer readAll(Long conversationId, Long userId, LocalDateTime localDateTime);
}
