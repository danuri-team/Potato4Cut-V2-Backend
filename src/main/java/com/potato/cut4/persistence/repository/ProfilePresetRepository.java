package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.ProfilePreset;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePresetRepository extends JpaRepository<ProfilePreset, UUID> {

  boolean existsProfilePresetByImgUrl(String url);
}
