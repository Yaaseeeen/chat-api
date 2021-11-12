package kz.komek.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "messages")
@EqualsAndHashCode(callSuper = true)
public class MessagesEntity extends AbstractEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "messages_seq")
  @SequenceGenerator(
      name = "messages_seq",
      sequenceName = "messages_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "conversation_id")
  private Long conversationId;

  @Column(name = "reply_on_message_id")
  private Long replyOnMessageId;

  @Column(name = "messaged_user_id")
  private Long messagedUserId;

  @Column(name = "message")
  private String message;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "messaged_user_id", updatable = false, insertable = false)
  private UserEntity userEntity;

}
