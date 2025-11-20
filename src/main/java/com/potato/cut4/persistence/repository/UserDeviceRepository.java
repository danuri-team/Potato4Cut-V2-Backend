package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.UserDevice;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

  Optional<UserDevice> findByFcmToken(String fcmToken);

  List<UserDevice> findByUser(User user);

  @Query("SELECT ud.fcmToken FROM UserDevice ud WHERE ud.user.id IN :userIds")
  List<String> findFcmTokensByUserIds(@Param("userIds") List<UUID> userIds);

  @Query("SELECT ud.fcmToken FROM UserDevice ud")
  List<String> findFcmTokens();

  @Modifying
  @Query("DELETE FROM UserDevice f WHERE f.fcmToken IN :tokens")
  int deleteAllByTokenIn(@Param("tokens") List<String> tokens);
}
