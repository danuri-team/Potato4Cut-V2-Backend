package com.potato.cut4.common.util;

public class GetCurrentEnvironment {

  public static String execute() {
    String profile = System.getProperty("spring.profiles.active");
    if (profile.equalsIgnoreCase("prod")) {
      return "prod";
    } else {
      return "dev";
    }
  }
}
