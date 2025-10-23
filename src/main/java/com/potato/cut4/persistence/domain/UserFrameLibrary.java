package com.potato.cut4.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "user_frame_libraries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFrameLibrary {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "frame_id", nullable = false)
  private Frame frame;

  @Column(nullable = false)
  private boolean bookmarked = false;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime addedAt;

  @Column
  private LocalDateTime lastUsedAt;

  @Builder
  public UserFrameLibrary(User user, Frame frame, boolean bookmarked) {
    this.user = user;
    this.frame = frame;
    this.bookmarked = bookmarked;
  }

  public void toggleBookmark() {
    this.bookmarked = !this.bookmarked;
  }

  public void updateLastUsedAt() {
    this.lastUsedAt = LocalDateTime.now();
  }
}
