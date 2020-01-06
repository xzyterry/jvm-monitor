package com.jawnho.service;

import com.google.common.base.Preconditions;
import com.jawnho.domain.HostInfo;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author jawnho
 * @date 2020/1/5
 */
public class HostInfoService implements IHostInfoService {

  private final MongoTemplate mongoTemplate;

  public HostInfoService(MongoTemplate configMongoTemplate) {
    Preconditions.checkNotNull(configMongoTemplate);
    this.mongoTemplate = configMongoTemplate;
  }

  @Override
  public List<HostInfo> findAll() {
    return mongoTemplate.findAll(HostInfo.class);
  }
}
