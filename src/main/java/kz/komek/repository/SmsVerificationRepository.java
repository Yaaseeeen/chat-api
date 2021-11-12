package kz.komek.repository;

import kz.komek.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsVerificationRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByPhone(String phone);
}
