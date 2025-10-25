package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {

  Page<Photo> findByUserAndDeletedFalse(User user, Pageable pageable);

  @Query("SELECT p FROM Photo p LEFT JOIN FETCH p.photoCuts WHERE p.id = :id AND p.deleted = false")
  Optional<Photo> findByIdAndDeletedFalseWithCuts(@Param("id") UUID id);

  Optional<Photo> findByIdAndDeletedFalse(UUID id);

  Optional<Photo> findByIdAndUserAndDeletedFalse(UUID id, User user);
}
