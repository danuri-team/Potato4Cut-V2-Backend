package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.type.UserRole;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

  private UUID userId;
  private String nickname;
  private String email;
  private String profileImageUrl;
  private UserRole role;
  private TokenResponse token;
  private boolean isNewUser;
}
