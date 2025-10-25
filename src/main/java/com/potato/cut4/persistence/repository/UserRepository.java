package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.SocialProvider;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findBySocialProviderAndSocialId(SocialProvider provider, String socialId);

  Optional<User> findByEmail(String email);

  boolean existsByNickname(String nickname);

  Optional<User> findByIdAndDeletedFalse(UUID id);

  @Query("SELECT u.id FROM User u WHERE u.deleted = false")
  List<UUID> findAllActiveUserIds();
}
