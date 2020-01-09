package com.jawnho.config;

import com.google.common.base.Preconditions;
import com.jawnho.service.GcRecordService;
import com.jawnho.service.HostInfoService;
import com.jawnho.service.HostServiceService;
import com.jawnho.service.IGcRecordService;
import com.jawnho.service.IHostInfoService;
import com.jawnho.service.IHostServiceService;
import com.jawnho.service.IJstackRecordService;
import com.jawnho.service.JstackRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author jawnho
 * @date 2019/11/5
 */
@Configuration
public class ServiceConfig {


  private final MongoTemplate configMongoTemplate;
  private final MongoTemplate mainMongoTemplate;

  @Autowired
  public ServiceConfig(
    @Qualifier(value = "configMongoTemplate") MongoTemplate configMongoTemplate,
    @Qualifier(value = "mainMongoTemplate") MongoTemplate mainMongoTemplate) {
    Preconditions.checkNotNull(configMongoTemplate);
    this.configMongoTemplate = configMongoTemplate;

    Preconditions.checkNotNull(mainMongoTemplate);
    this.mainMongoTemplate = mainMongoTemplate;
  }

  //region config

  @Bean
  public IHostInfoService hostInfoService(){
    return new HostInfoService(configMongoTemplate);
  }

  //endregion

  // region main

  @Bean
  public IGcRecordService gcRecordService(){
    return new GcRecordService(mainMongoTemplate);
  }

  @Bean
  public IJstackRecordService jstackRecordService(){
    return  new JstackRecordService(mainMongoTemplate);
  }

  @Bean
  public IHostServiceService hostServiceService(){
    return new HostServiceService(mainMongoTemplate);
  }

  // endregion

}
