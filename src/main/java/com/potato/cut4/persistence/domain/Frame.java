package com.potato.cut4.persistence.domain;

import com.potato.cut4.persistence.domain.type.FrameCategory;
import com.potato.cut4.persistence.domain.type.FrameStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "frames")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Frame {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id", nullable = false)
  private Creator creator;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false, length = 500)
  private String frameImageUrl;

  @Column(nullable = false, length = 500)
  private String previewImageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FrameCategory category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FrameStatus status;

  @Column(length = 1000)
  private String rejectionReason;

  @Column(nullable = false)
  private long downloadCount = 0;

  @Column(nullable = false)
  private long likeCount = 0;

  @Column(nullable = false)
  private long viewCount = 0;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Column
  private LocalDateTime approvedAt;

  @OneToMany(mappedBy = "frame")
  private List<FrameTag> frameTags = new ArrayList<>();

  @OneToMany(mappedBy = "frame")
  private List<FrameLike> frameLikes = new ArrayList<>();

  @OneToMany(mappedBy = "frame")
  private List<FrameComment> frameComments = new ArrayList<>();

  @OneToMany(mappedBy = "frame")
  private List<UserFrameLibrary> userFrameLibraries = new ArrayList<>();

  @Builder
  public Frame(Creator creator, String title, String description, String frameImageUrl,
      String previewImageUrl, FrameCategory category, FrameStatus status) {
    this.creator = creator;
    this.title = title;
    this.description = description;
    this.frameImageUrl = frameImageUrl;
    this.previewImageUrl = previewImageUrl;
    this.category = category;
    this.status = status;
  }

  public void approve() {
    this.status = FrameStatus.APPROVED;
    this.approvedAt = LocalDateTime.now();
    this.rejectionReason = null;
  }

  public void reject(String reason) {
    this.status = FrameStatus.REJECTED;
    this.rejectionReason = reason;
    this.approvedAt = null;
  }

  public void hide() {
    this.status = FrameStatus.HIDDEN;
  }

  public void incrementDownloadCount() {
    this.downloadCount++;
  }

  public void incrementViewCount() {
    this.viewCount++;
  }

  public void incrementLikeCount() {
    this.likeCount++;
  }

  public void decrementLikeCount() {
    if (this.likeCount > 0) {
      this.likeCount--;
    }
  }

  public void updateInfo(String title, String description, FrameCategory category) {
    if (title != null) {
      this.title = title;
    }
    if (description != null) {
      this.description = description;
    }
    if (category != null) {
      this.category = category;
    }
  }
}
