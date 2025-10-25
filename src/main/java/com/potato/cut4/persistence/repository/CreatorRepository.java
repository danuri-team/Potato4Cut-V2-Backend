package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Creator;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.CreatorStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, UUID> {

  Optional<Creator> findByUser(User user);

  boolean existsByUser(User user);

  Page<Creator> findByStatus(CreatorStatus status, Pageable pageable);
}
