package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {

  Page<Photo> findByUserAndDeletedFalse(User user, Pageable pageable);

  Optional<Photo> findByIdAndDeletedFalse(UUID id);
}
