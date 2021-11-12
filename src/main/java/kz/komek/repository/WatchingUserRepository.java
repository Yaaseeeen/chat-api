package kz.komek.repository;

import kz.komek.entity.WatchingUsersEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WatchingUserRepository extends
    CrudRepository<WatchingUsersEntity, Long> {

  List<WatchingUsersEntity> findAllByConversationId(Long convId);

  WatchingUsersEntity findAllByConversationIdAndUserId(Long convId, Long userId);

  List<WatchingUsersEntity> findAllByUserId(Long userId);

  @Query(value = "SELECT c.id as conversation_id, CAST (COUNT(m.message) AS INTEGER) as count " +
      "FROM chat_example.conversation AS c " +
      "LEFT JOIN chat_example.messages AS m ON c.id = m.conversation_id " +
      "WHERE c.id IN :ids " +
      "GROUP BY c.id", nativeQuery = true)
  List<Map<String, Object>> countMessages(@Param("ids") List<Long> ids);

  @Query(value = "SELECT m.conversation_id, CAST (COUNT(m.id) AS INTEGER) as count " +
      " FROM chat_example.messages_read_by_users as m" +
      " WHERE read_by_user_id = :userId AND conversation_id IN :ids" +
      " GROUP BY m.conversation_id", nativeQuery = true)
  List<Map<String, Object>> readCountMessages(@Param("userId") Long userId,
                                              @Param("ids") List<Long> ids);


}
