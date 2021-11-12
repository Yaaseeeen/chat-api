package kz.komek.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "messages_read_by_users", uniqueConstraints =
@UniqueConstraint(columnNames = {"messages_id", "read_by_user_id", "conversation_id"} ))
@EqualsAndHashCode(callSuper = true)
@Builder
public class MessagesReadByUsersEntity extends AbstractEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "messages_read_by_users_seq")
  @SequenceGenerator(
      name = "messages_read_by_users_seq",
      sequenceName = "messages_read_by_users_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "messages_id")
  private Long messagesId;

  @Column(name = "read_by_user_id")
  private Long readByUserId;

  @Column(name = "conversation_id")
  private Long conversationId;
}
