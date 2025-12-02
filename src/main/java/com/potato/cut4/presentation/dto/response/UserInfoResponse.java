package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.type.SocialProvider;
import com.potato.cut4.persistence.domain.type.UserRole;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

  private UUID userId;
  private String nickname;
  private SocialProvider provider;
  private String email;
  private String profileImageUrl;
  private String bio;
  private UserRole role;
}
