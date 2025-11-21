package com.potato.cut4.common.integration.service;

import com.potato.cut4.common.integration.client.AppleOauthInfoFeignClient;
import com.potato.cut4.common.integration.dto.AppleInfoResDto;
import com.potato.cut4.common.integration.dto.ApplePublicKey;
import com.potato.cut4.common.integration.dto.ApplePublicKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleOauthInfoService {

  private final AppleOauthInfoFeignClient appleOauthInfoFeignClient;

  public AppleInfoResDto login(String identityToken) {
    try {
      ApplePublicKeys publicKeys = appleOauthInfoFeignClient.getPublicKeys();
      Claims claims = parseAndVerifyToken(identityToken, publicKeys);

      String sub = claims.getSubject();
      String email = claims.get("email", String.class);

      return new AppleInfoResDto(sub, email);
    } catch (Exception e) {
      log.error("Apple OAuth 사용자 정보 요청 중 예외 발생", e);
      throw new RuntimeException("Apple OAuth 사용자 정보 요청 중 예외가 발생했습니다.");
    }
  }

  private Claims parseAndVerifyToken(String identityToken, ApplePublicKeys publicKeys) {
    try {
      Map<String, Object> headers = Jwts.parser()
          .build()
          .parseSignedClaims(identityToken)
          .getHeader();

      String kid = (String) headers.get("kid");
      String alg = (String) headers.get("alg");

      ApplePublicKey matchedKey = publicKeys.keys().stream()
          .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("일치하는 Apple 공개 키를 찾을 수 없습니다."));

      PublicKey publicKey = generatePublicKey(matchedKey);

      return Jwts.parser()
          .verifyWith(publicKey)
          .build()
          .parseSignedClaims(identityToken)
          .getPayload();
    } catch (Exception e) {
      log.error("Apple Identity Token 검증 실패", e);
      throw new RuntimeException("Apple Identity Token 검증에 실패했습니다.");
    }
  }

  private PublicKey generatePublicKey(ApplePublicKey applePublicKey) {
    try {
      byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
      byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

      BigInteger n = new BigInteger(1, nBytes);
      BigInteger e = new BigInteger(1, eBytes);

      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");

      return keyFactory.generatePublic(publicKeySpec);
    } catch (Exception e) {
      log.error("Apple 공개 키 생성 실패", e);
      throw new RuntimeException("Apple 공개 키 생성에 실패했습니다.");
    }
  }
}
