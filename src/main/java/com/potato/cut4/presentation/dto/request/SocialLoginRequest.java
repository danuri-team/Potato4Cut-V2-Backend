package com.potato.cut4.presentation.dto.request;

import com.potato.cut4.persistence.domain.type.SocialProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequest {

  @NotNull(message = "소셜 로그인 제공자는 필수입니다.")
  private SocialProvider provider;

  @NotNull(message = "소셜 로그인 토큰은 필수입니다.")
  private String oauthToken;

  private String deviceToken;
}
