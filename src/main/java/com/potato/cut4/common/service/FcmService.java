package com.potato.cut4.common.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

  public void sendNotification(String token, String title, String body, String imageUrl) {
    try {
      Notification.Builder notificationBuilder = Notification.builder()
          .setTitle(title)
          .setBody(body);

      if (imageUrl != null && !imageUrl.isEmpty()) {
        notificationBuilder.setImage(imageUrl);
      }

      Message message = Message.builder()
          .setToken(token)
          .setNotification(notificationBuilder.build())
          .build();

      String response = FirebaseMessaging.getInstance().send(message);
      log.info("Successfully sent FCM message: {}", response);

    } catch (FirebaseMessagingException e) {
      log.error("Failed to send FCM message to token: {}", token, e);
    }
  }

  public List<String> sendMulticastNotification(
      List<String> tokens, String title, String body, String imageUrl) {

    if (tokens == null || tokens.isEmpty()) {
      log.warn("No tokens provided for multicast notification");
      return List.of();
    }

    try {
      Notification notification = (imageUrl == null || imageUrl.isEmpty())
          ? Notification.builder().setTitle(title).setBody(body).build()
          : Notification.builder().setTitle(title).setBody(body).setImage(imageUrl).build();

      MulticastMessage message = MulticastMessage.builder()
          .addAllTokens(tokens)
          .setNotification(notification)
          .build();

      BatchResponse response = FirebaseMessaging.getInstance()
          .sendEachForMulticast(message);

      int size = tokens.size();
      List<SendResponse> responses = response.getResponses();

      log.info("Successfully sent FCM multicast message: {} success, {} failure",
          response.getSuccessCount(), response.getFailureCount());

      List<String> failedTokens = new ArrayList<>(response.getFailureCount());

      for (int i = 0; i < size; i++) {
        SendResponse res = responses.get(i);
        if (!res.isSuccessful()) {
          String failedToken = tokens.get(i);
          failedTokens.add(failedToken);

          Exception ex = res.getException();
          log.warn("Failed to send to token: {}, error: {}", failedToken,
              (ex != null ? ex.getMessage() : "unknown"));
        }
      }

      return failedTokens;

    } catch (FirebaseMessagingException e) {
      log.error("Failed to send FCM multicast message", e);
      return tokens;
    }
  }

}
