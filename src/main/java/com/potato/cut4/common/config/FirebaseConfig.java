package com.potato.cut4.common.config;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FirebaseConfig {

  @Value("${firebase.credentials.key}")
  private String firebaseCredentialsKey;

  @PostConstruct
  public void initialize() {

    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.create(
            AccessToken.newBuilder().setTokenValue(firebaseCredentialsKey).build()))
        .build();

    if (FirebaseApp.getApps().isEmpty()) {
      FirebaseApp.initializeApp(options);
      log.info("Firebase Admin SDK initialized successfully");
    }
  }
}
