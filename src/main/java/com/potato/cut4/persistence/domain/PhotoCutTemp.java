package com.potato.cut4.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "photo_cut_temps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoCutTemp {

  @Id
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private int cutOrder;

  @Column(nullable = false, length = 500)
  private String originalImageUrl;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  public PhotoCutTemp(User user, int cutOrder, String originalImageUrl) {
    this.id = UUID.randomUUID();
    this.user = user;
    this.cutOrder = cutOrder;
    this.originalImageUrl = originalImageUrl;
  }
}
