package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, UUID> {

  Optional<Tag> findByName(String name);

  List<Tag> findTop10ByOrderByUsageCountDesc();
}
