package kz.komek.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "conversation")
@EqualsAndHashCode(callSuper = true)
public class ConversationEntity extends AbstractEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "conv_seq")
  @SequenceGenerator(
      name = "conv_seq",
      sequenceName = "conv_id_seq", allocationSize = 1)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "created_user_id")
  private Long createdUserId;

  @Column(name = "is_private")
  private boolean isPrivate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_user_id", updatable = false, insertable = false)
  private UserEntity createdUser;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id", updatable = false, insertable = false)
  private List<MessagesEntity> messagesEntityList;
}
