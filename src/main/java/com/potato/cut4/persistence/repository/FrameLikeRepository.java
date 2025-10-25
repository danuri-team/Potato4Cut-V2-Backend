package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameLike;
import com.potato.cut4.persistence.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameLikeRepository extends JpaRepository<FrameLike, UUID> {

  Optional<FrameLike> findByUserAndFrame(User user, Frame frame);

  boolean existsByUserAndFrame(User user, Frame frame);

  long countByFrame(Frame frame);
}
