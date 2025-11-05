package com.potato.cut4.application.service;

import com.potato.cut4.common.integration.dto.GoogleInfoResDto;
import com.potato.cut4.common.integration.service.GoogleOauthInfoService;
import com.potato.cut4.common.security.JwtTokenProvider;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.UserRole;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.request.SocialLoginRequest;
import com.potato.cut4.presentation.dto.response.AuthResponse;
import com.potato.cut4.presentation.dto.response.TokenResponse;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final GoogleOauthInfoService googleOauthInfoService;

  private static final Random RANDOM = new Random();

  @Transactional
  public AuthResponse socialLogin(SocialLoginRequest request) {
    GoogleInfoResDto oauthDto = googleOauthInfoService.login(request.getOauthToken());

    User user = userRepository.findBySocialProviderAndSocialId(
            request.getProvider(),
            oauthDto.sub()
        )
        .orElse(null);

    boolean isNewUser = false;

    if (user == null) {
      isNewUser = true;
      user = createUser(request, oauthDto);
    } else if (user.isDeleted()) {
      isNewUser = true;
      user.restore();
      log.info("User restored: userId={}, email={}", user.getId(), user.getEmail());
    }

    String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getRole());

    return AuthResponse.builder()
        .userId(user.getId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .profileImageUrl(user.getProfileImageUrl())
        .role(user.getRole())
        .token(TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build())
        .isNewUser(isNewUser)
        .build();
  }

  @Transactional
  public TokenResponse refreshToken(String refreshToken) {
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
    }

    UUID userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    String newAccessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole());
    String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getRole());

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build();
  }

  private User createUser(SocialLoginRequest request, GoogleInfoResDto oauthDto) {
    User user = User.builder()
        .nickname("옹심이 " + RANDOM.nextInt(100000))
        .email(oauthDto.email())
        .socialProvider(request.getProvider())
        .socialId(oauthDto.sub())
        .role(UserRole.USER)
        .build();

    return userRepository.save(user);
  }
}
