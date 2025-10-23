package com.potato.cut4.persistence.domain;

import com.potato.cut4.persistence.domain.type.CreatorStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "creators")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Creator {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CreatorStatus status;

  @Column(length = 1000)
  private String rejectionReason;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime appliedAt;

  @Column
  private LocalDateTime approvedAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "creator")
  private List<Frame> frames = new ArrayList<>();

  @Builder
  public Creator(User user, CreatorStatus status) {
    this.user = user;
    this.status = status;
  }

  public void approve() {
    this.status = CreatorStatus.APPROVED;
    this.approvedAt = LocalDateTime.now();
    this.rejectionReason = null;
  }

  public void reject(String reason) {
    this.status = CreatorStatus.REJECTED;
    this.rejectionReason = reason;
    this.approvedAt = null;
  }
}
