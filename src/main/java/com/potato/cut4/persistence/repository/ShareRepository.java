package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Share;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, UUID> {

  Optional<Share> findByCode(String code);
}
