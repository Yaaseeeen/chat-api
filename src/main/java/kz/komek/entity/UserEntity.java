package kz.komek.entity;

import kz.komek.model.enums.UserStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "verification_code", nullable = false)
  private String verificationCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "verification_status", nullable = false)
  private UserStatus status;

  @Column(name = "message")
  private String message;
}
