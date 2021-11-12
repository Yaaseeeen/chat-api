package kz.komek.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity implements Serializable {

  @Column(name = "date_created", updatable = false)
  LocalDateTime dateCreated;

  @Column(name = "date_updated")
  LocalDateTime dateUpdated;

  @PrePersist
  public void toCreate() {
    setDateCreated(LocalDateTime.now());
    setDateUpdated(getDateCreated());
  }

  @PreUpdate
  public void toUpdate() {
    setDateUpdated(LocalDateTime.now());
  }

}
