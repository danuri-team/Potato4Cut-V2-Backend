package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.PhotoCut;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoCutRepository extends JpaRepository<PhotoCut, UUID> {

  List<PhotoCut> findByPhotoOrderByCutOrderAsc(Photo photo);
}
