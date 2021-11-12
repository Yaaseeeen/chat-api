package kz.komek.entity;

import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "watching_users")
@EqualsAndHashCode(callSuper = true)
public class WatchingUsersEntity extends AbstractEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "watching_users_seq")
  @SequenceGenerator(
      name = "watching_users_seq",
      sequenceName = "watching_users_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "conversation_id")
  private Long conversationId;

  @Column(name = "user_id")
  private Long userId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", updatable = false, insertable = false)
  private UserEntity userEntity;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "conversation_id", updatable = false, insertable = false)
  private ConversationEntity conversationEntity;
}
