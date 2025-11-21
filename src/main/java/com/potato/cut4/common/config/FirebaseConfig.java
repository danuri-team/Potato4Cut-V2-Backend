package com.potato.cut4.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class FirebaseConfig {

  @PostConstruct
  public void initialize() {
    try {
      InputStream serviceAccount = new ClassPathResource(
          "google-credentials.json").getInputStream();

      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("Firebase Admin SDK initialized successfully");
      }
    } catch (IOException e) {
      log.error("Failed to initialize Firebase Admin SDK", e);
      throw new RuntimeException("Failed to initialize Firebase", e);
    }
  }
}
