package com.potato.cut4.presentation.controller;


import com.potato.cut4.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  public ResponseEntity<ApiResponse<Void>> healthCheck() {
    return ResponseEntity.ok(ApiResponse.success());
  }
}
