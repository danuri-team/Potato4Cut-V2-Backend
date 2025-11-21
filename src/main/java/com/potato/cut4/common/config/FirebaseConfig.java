package com.potato.cut4.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FirebaseConfig {

  @Value("${firebase.credentials.key}")
  private String firebaseCredentialsBase64;

  @PostConstruct
  public void initialize() {
    try {
      if (FirebaseApp.getApps().isEmpty()) {

        byte[] jsonBytes = Base64.getDecoder().decode(firebaseCredentialsBase64);

        String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String projectId = jsonObject.get("project_id").getAsString();

        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new ByteArrayInputStream(jsonBytes)
        );

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .setProjectId(projectId)
            .build();

        FirebaseApp.initializeApp(options);
      }
    } catch (Exception e) {
      log.error("Failed to initialize Firebase Admin SDK", e);
      throw new RuntimeException("Failed to initialize Firebase Admin SDK", e);
    }
  }
}
