package com.potato.cut4.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @Column(nullable = false)
  private long usageCount = 0;

  @OneToMany(mappedBy = "tag")
  private List<FrameTag> frameTags = new ArrayList<>();

  @Builder
  public Tag(String name) {
    this.name = name;
  }

  public void incrementUsageCount() {
    this.usageCount++;
  }

  public void decrementUsageCount() {
    if (this.usageCount > 0) {
      this.usageCount--;
    }
  }
}
