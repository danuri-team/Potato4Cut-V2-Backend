package com.potato.cut4.common.integration.client;

import com.potato.cut4.common.integration.dto.ApplePublicKeys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleOauthInfoClient", url = "https://appleid.apple.com")
public interface AppleOauthInfoFeignClient {

  @GetMapping("/auth/keys")
  ApplePublicKeys getPublicKeys();
}
