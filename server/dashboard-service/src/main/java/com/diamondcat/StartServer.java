package com.diamondcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Jawnho
 * @date 2019/11/5
 */

@SpringBootApplication(
  exclude = {
    MongoDataAutoConfiguration.class,
    MongoReactiveDataAutoConfiguration.class
  }
)
@EnableScheduling
@PropertySource(value = "file:${WS_SERV_BASE}/conf/dashboard-service.conf")
public class StartServer {

  public static void main(String[] args) {
    SpringApplication.run(StartServer.class, args);
  }
}
