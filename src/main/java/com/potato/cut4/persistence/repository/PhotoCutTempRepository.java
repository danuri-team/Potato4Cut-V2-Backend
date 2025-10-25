package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.PhotoCutTemp;
import com.potato.cut4.persistence.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoCutTempRepository extends JpaRepository<PhotoCutTemp, UUID> {

  List<PhotoCutTemp> findByIdIn(List<UUID> ids);

  // 24시간 이상 된 임시 파일 삭제용
  void deleteByCreatedAtBefore(LocalDateTime dateTime);

  void deleteByUser(User user);
}
