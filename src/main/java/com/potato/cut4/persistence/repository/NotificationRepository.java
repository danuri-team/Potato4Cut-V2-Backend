package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Notification;
import com.potato.cut4.persistence.domain.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  Page<Notification> findByUser(User user, Pageable pageable);

  Page<Notification> findByUserAndIsReadFalse(User user, Pageable pageable);

  long countByUserAndIsReadFalse(User user);
}
