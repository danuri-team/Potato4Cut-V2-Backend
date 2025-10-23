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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photo_cuts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoCut {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "photo_id", nullable = false)
  private Photo photo;

  @Column(nullable = false)
  private int cutOrder;

  @Column(nullable = false, length = 500)
  private String originalImageUrl;

  @Builder
  public PhotoCut(Photo photo, int cutOrder, String originalImageUrl) {
    this.photo = photo;
    this.cutOrder = cutOrder;
    this.originalImageUrl = originalImageUrl;
  }
}
