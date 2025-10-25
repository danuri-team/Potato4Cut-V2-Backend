package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameComment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameCommentRepository extends JpaRepository<FrameComment, UUID> {

  Page<FrameComment> findByFrameAndDeletedFalse(Frame frame, Pageable pageable);

  Optional<FrameComment> findByIdAndDeletedFalse(UUID id);

  long countByFrameAndDeletedFalse(Frame frame);
}
