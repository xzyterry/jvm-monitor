package com.diamondcat.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author jawnho
 * @date 2019/11/5
 * @desc 数据库配置
 */
@Configuration
@EnableScheduling
public class DalConfig {

  private final Environment env;

  public DalConfig(Environment env) {
    this.env = env;
  }

  //region mongodb connection

  @Bean
  public MongoClientOptions mongoClientOptions() {
    MongoClientOptions.Builder builder = MongoClientOptions.builder();
    builder.connectionsPerHost(1000);
    builder.threadsAllowedToBlockForConnectionMultiplier(1000);
    builder.maxWaitTime(180000);
    builder.connectTimeout(2000);
    builder.socketTimeout(120000);
    builder.writeConcern(new WriteConcern(1, 10000));

    return builder.build();
  }

  //endregion

  //region config

  @Bean
  public MongoClient configMongo() {
    String host = env.getProperty("mongoDB.config.host");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(host));
    String port = env.getProperty("mongoDB.config.port");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(port));
    String username = env.getProperty("mongoDB.config.username");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(username));
    String password = env.getProperty("mongoDB.config.password");

    String authDB = env.getProperty("mongoDB.config.authDB");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(authDB));

    if (Strings.isNullOrEmpty(password)) {
      return new MongoClient(host, Integer.valueOf(port));
    } else {
      return new MongoClient(
        new ServerAddress(
          host,
          Integer.valueOf(port.trim())
        ),
        MongoCredential.createCredential(
          username,
          authDB,
          password.trim().toCharArray()
        ),
        mongoClientOptions()
      );
    }
  }

  @Bean
  public SimpleMongoDbFactory configMongoDbFactory() {
    String database = env.getProperty("mongoDB.config.database");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(database));
    return new SimpleMongoDbFactory(configMongo(), database);
  }

  @Bean(name = "configMongoTemplate")
  public MongoTemplate configMongoTemplate() {
    return new MongoTemplate(configMongoDbFactory());
  }

  //endregion

  //region main

  @Bean
  public MongoClient mainMongo() {
    String host = env.getProperty("mongoDB.main.host");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(host));
    String port = env.getProperty("mongoDB.main.port");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(port));
    String username = env.getProperty("mongoDB.main.username");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(username));
    String password = env.getProperty("mongoDB.main.password");

    String authDB = env.getProperty("mongoDB.main.authDB");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(authDB));

    if (Strings.isNullOrEmpty(password)) {
      return new MongoClient(host, Integer.valueOf(port));
    } else {
      return new MongoClient(
          new ServerAddress(
              host,
              Integer.valueOf(port.trim())
          ),
          MongoCredential.createCredential(
              username,
              authDB,
              password.trim().toCharArray()
          ),
          mongoClientOptions()
      );
    }
  }

  @Bean
  public SimpleMongoDbFactory mainMongoDbFactory() {
    String database = env.getProperty("mongoDB.main.database");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(database));
    return new SimpleMongoDbFactory(mainMongo(), database);
  }

  @Bean(name = "mainMongoTemplate")
  public MongoTemplate mainMongoTemplate() {
    return new MongoTemplate(mainMongoDbFactory());
  }

  //endregion

}
