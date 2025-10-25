package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Creator;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.type.FrameCategory;
import com.potato.cut4.persistence.domain.type.FrameStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FrameRepository extends JpaRepository<Frame, UUID> {

  Page<Frame> findByStatus(FrameStatus status, Pageable pageable);

  Page<Frame> findByStatusAndCategory(FrameStatus status, FrameCategory category,
      Pageable pageable);

  Page<Frame> findByCreator(Creator creator, Pageable pageable);

  Optional<Frame> findByIdAndCreator(UUID id, Creator creator);

  @Query("SELECT f FROM Frame f WHERE f.status = :status AND "
      + "(LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
      + "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
  Page<Frame> searchByKeyword(@Param("status") FrameStatus status,
      @Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT f FROM Frame f "
      + "JOIN f.frameTags ft "
      + "JOIN ft.tag t "
      + "WHERE f.status = :status AND t.name = :tagName")
  Page<Frame> findByStatusAndTagName(@Param("status") FrameStatus status,
      @Param("tagName") String tagName, Pageable pageable);
}
