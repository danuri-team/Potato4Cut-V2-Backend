package com.potato.cut4.common.security;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

  public static UUID getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("인증되지 않은 사용자입니다.");
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UUID uuid) {
      return uuid;
    }

    throw new IllegalStateException("유효하지 않은 인증 정보입니다.");
  }
}
