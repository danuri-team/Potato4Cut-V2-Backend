package com.potato.cut4.persistence.domain;

import com.potato.cut4.persistence.domain.type.PhotoShareType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shares")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Share {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, length = 10)
  private String code;

  @OneToOne
  @JoinColumn(name = "photo_id", nullable = false)
  private Photo photo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PhotoShareType type;

  @Column
  private LocalDateTime expireAt;

  @Builder
  public Share(String code, Photo photo, LocalDateTime expireAt, PhotoShareType type) {
    this.code = code;
    this.expireAt = expireAt;
    this.photo = photo;
    this.type = type;
  }

  public void update(LocalDateTime expireAt, PhotoShareType type) {
    this.expireAt = expireAt;
    this.type = type;
  }
}
