package com.potato.cut4.persistence.domain;

import com.potato.cut4.persistence.domain.type.SocialProvider;
import com.potato.cut4.persistence.domain.type.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String nickname;

  @Column(length = 500)
  private String profileImageUrl;

  @Column(length = 500)
  private String bio = "";

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Enumerated(EnumType.STRING)
  private SocialProvider socialProvider;

  @Column(unique = true)
  private String socialId;

  @Column(unique = true, length = 100)
  private String email;

  @Column(nullable = false)
  private boolean deleted = false;

  @Column(nullable = false)
  private boolean notificationEnabled = false;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "user")
  private Creator creator;

  @OneToMany(mappedBy = "user")
  private List<Photo> photos = new ArrayList<>();

  @OneToMany(mappedBy = "follower")
  private List<Follow> following = new ArrayList<>();

  @OneToMany(mappedBy = "following")
  private List<Follow> followers = new ArrayList<>();

  @Builder
  public User(String nickname, String profileImageUrl, String bio, UserRole role,
      SocialProvider socialProvider, String socialId, String email) {
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
    this.bio = bio;
    this.role = role;
    this.socialProvider = socialProvider;
    this.socialId = socialId;
    this.email = email;
  }

  public void updateProfile(String nickname, String profileImageUrl, String bio) {
    if (nickname != null) {
      this.nickname = nickname;
    }
    if (profileImageUrl != null) {
      this.profileImageUrl = profileImageUrl;
    }
    if (bio != null) {
      this.bio = bio;
    }
  }

  public void updateRole(UserRole role) {
    this.role = role;
  }

  public void updateNotifiactionEnabled() {
    this.notificationEnabled = !notificationEnabled;
  }

  public void delete() {
    this.deleted = true;
  }

  public void restore() {
    this.deleted = false;
  }
}
