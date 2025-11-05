package com.potato.cut4.application.service;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameTag;
import com.potato.cut4.persistence.domain.Tag;
import com.potato.cut4.persistence.repository.FrameTagRepository;
import com.potato.cut4.persistence.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

  private final TagRepository tagRepository;
  private final FrameTagRepository frameTagRepository;

  @Transactional
  public void addTagsToFrame(Frame frame, List<String> tagNames) {
    if (tagNames == null || tagNames.isEmpty()) {
      return;
    }

    for (String tagName : tagNames) {
      String normalizedTagName = tagName.trim().toLowerCase();

      if (normalizedTagName.isEmpty()) {
        continue;
      }

      Tag tag = tagRepository.findByName(normalizedTagName)
          .orElseGet(() -> {
            Tag newTag = Tag.builder()
                .name(normalizedTagName)
                .build();
            return tagRepository.save(newTag);
          });

      FrameTag frameTag = FrameTag.builder()
          .frame(frame)
          .tag(tag)
          .build();
      frameTagRepository.save(frameTag);

      tag.incrementUsageCount();
    }
  }

  @Transactional
  public void updateFrameTags(Frame frame, List<String> newTagNames) {
    List<Tag> oldTags = frame.getFrameTags().stream()
        .map(FrameTag::getTag)
        .toList();

    frameTagRepository.deleteByFrame(frame);

    for (Tag tag : oldTags) {
      tag.decrementUsageCount();
    }

    addTagsToFrame(frame, newTagNames);
  }

  public List<Tag> getPopularTags() {
    return tagRepository.findTop10ByOrderByUsageCountDesc();
  }
}
