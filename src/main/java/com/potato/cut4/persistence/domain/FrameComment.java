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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "frame_comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FrameComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "frame_id", nullable = false)
  private Frame frame;

  @Column(nullable = false, length = 1000)
  private String content;

  @Column(nullable = false)
  private boolean deleted = false;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public FrameComment(User user, Frame frame, String content) {
    this.user = user;
    this.frame = frame;
    this.content = content;
  }

  public void updateContent(String content) {
    this.content = content;
  }

  public void delete() {
    this.deleted = true;
  }
}
