package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.UserDevice;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

  Optional<UserDevice> findByFcmToken(String fcmToken);

  List<UserDevice> findByUserAndActiveTrue(User user);

  @Query("SELECT ud.fcmToken FROM UserDevice ud WHERE ud.user.id IN :userIds AND ud.active = true")
  List<String> findFcmTokensByUserIds(@Param("userIds") List<UUID> userIds);

  @Query("SELECT ud.fcmToken FROM UserDevice ud WHERE ud.active = true")
  List<String> findAllActiveFcmTokens();
}
