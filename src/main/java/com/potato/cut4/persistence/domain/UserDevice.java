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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "user_devices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDevice {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, unique = true, length = 500)
  private String fcmToken;

  @Column(length = 100)
  private String deviceType; // iOS, Android

  @Column(length = 100)
  private String deviceModel;

  @Column(nullable = false)
  private boolean active = true;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public UserDevice(User user, String fcmToken, String deviceType, String deviceModel) {
    this.user = user;
    this.fcmToken = fcmToken;
    this.deviceType = deviceType;
    this.deviceModel = deviceModel;
  }

  public void updateToken(String fcmToken) {
    this.fcmToken = fcmToken;
  }

  public void deactivate() {
    this.active = false;
  }
}
