package com.potato.cut4.persistence.domain;

import com.potato.cut4.persistence.domain.type.ReportStatus;
import com.potato.cut4.persistence.domain.type.ReportType;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reporter_id", nullable = false)
  private User reporter;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportType type;

  @Column
  private UUID targetId;

  @Column(nullable = false, length = 1000)
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportStatus status;

  @Column(length = 1000)
  private String adminNote;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processed_by")
  private User processedBy;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Column
  private LocalDateTime processedAt;

  @Builder
  public Report(User reporter, ReportType type, UUID targetId, String reason,
      ReportStatus status) {
    this.reporter = reporter;
    this.type = type;
    this.targetId = targetId;
    this.reason = reason;
    this.status = status;
  }

  public void accept(User admin, String adminNote) {
    this.status = ReportStatus.ACCEPTED;
    this.processedBy = admin;
    this.adminNote = adminNote;
    this.processedAt = LocalDateTime.now();
  }

  public void reject(User admin, String adminNote) {
    this.status = ReportStatus.REJECTED;
    this.processedBy = admin;
    this.adminNote = adminNote;
    this.processedAt = LocalDateTime.now();
  }
}
