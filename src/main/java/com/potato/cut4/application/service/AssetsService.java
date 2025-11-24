package com.potato.cut4.application.service;

import com.potato.cut4.persistence.domain.ProfilePreset;
import com.potato.cut4.persistence.repository.ProfilePresetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetsService {

  private final ProfilePresetRepository profilePresetRepository;

  public List<ProfilePreset> getProfilePresets() {
    return profilePresetRepository.findAll();
  }
}
