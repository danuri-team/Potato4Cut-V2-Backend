package com.potato.cut4.common.integration.service;

import com.potato.cut4.common.integration.client.GoogleOauthInfoFeignClient;
import com.potato.cut4.common.integration.dto.GoogleInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOauthInfoService {

  private final GoogleOauthInfoFeignClient googleOauthInfoFeignClient;

  public GoogleInfoResDto login(String accessToken) {
    GoogleInfoResDto infoDto;

    try {
      infoDto = getInfo(accessToken);
    } catch (Exception e) {
      throw new RuntimeException("google oauth 사용자 정보 요청 중 예외가 발생했습니다.");
    }

    return infoDto;
  }

  private GoogleInfoResDto getInfo(String accessToken) {
    return googleOauthInfoFeignClient.getInfo(
        "Bearer " + accessToken
    );
  }

}