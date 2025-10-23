package com.potato.cut4.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "photos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "frame_id")
  private Frame frame;

  @Column(nullable = false, length = 500)
  private String composedImageUrl;

  @Column(nullable = false)
  private boolean deleted = false;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "photo")
  private List<PhotoCut> photoCuts = new ArrayList<>();

  @Builder
  public Photo(User user, Frame frame, String composedImageUrl) {
    this.user = user;
    this.frame = frame;
    this.composedImageUrl = composedImageUrl;
  }

  public void delete() {
    this.deleted = true;
  }
}
