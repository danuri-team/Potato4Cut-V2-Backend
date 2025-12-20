package com.potato.cut4.common.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class H2Config {

  private Server tcpServer;

  @PostConstruct
  public void start() throws SQLException {
    tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8089").start();
  }

  @PreDestroy
  public void stop() {
    if (tcpServer != null) {
      tcpServer.stop();
    }
  }
}