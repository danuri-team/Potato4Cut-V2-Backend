package com.potato.cut4.common.config;

import com.potato.cut4.common.exception.CustomException;
import io.sentry.SentryOptions.BeforeSendCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Configuration
public class SentryConfiguration {

  @Bean
  public BeforeSendCallback beforeSendCallback() {
    return (event, hint) -> {
      Throwable throwable = hint.getAs("exception", Throwable.class);
      if (throwable == null) {
        return event;
      }

      if (shouldIgnore(throwable)) {
        return null;
      }

      return event;
    };
  }

  private boolean shouldIgnore(Throwable throwable) {
    Throwable current = throwable;
    while (current != null) {
      if (current instanceof CustomException
          || current instanceof MethodArgumentNotValidException
          || current instanceof AuthenticationException
          || current instanceof AccessDeniedException) {
        return true;
      }
      current = current.getCause();
    }
    return false;
  }
}
