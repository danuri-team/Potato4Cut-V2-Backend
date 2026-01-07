package com.potato.cut4.common.util;

import java.security.SecureRandom;

public class ShortCodeGenerator {

  private static final char[] CHARSET =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  private static final SecureRandom RANDOM = new SecureRandom();

  public static String generate(int length) {
    if (length <= 0) {
      throw new IllegalArgumentException("length must be greater than 0");
    }

    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(CHARSET[RANDOM.nextInt(CHARSET.length)]);
    }
    return sb.toString();
  }
}
