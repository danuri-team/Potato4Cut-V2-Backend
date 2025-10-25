package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameTag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameTagRepository extends JpaRepository<FrameTag, UUID> {

  void deleteByFrame(Frame frame);
}
