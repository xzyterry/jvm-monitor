package com.jawnho.service;

import com.google.common.base.Preconditions;
import com.jawnho.domain.JstackRecord;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public class JstackRecordService implements IJstackRecordService {

  private final MongoTemplate mongoTemplate;

  public JstackRecordService(MongoTemplate mainMongoTemplate) {
    Preconditions.checkNotNull(mainMongoTemplate);
    this.mongoTemplate = mainMongoTemplate;
  }

  @Override
  public void insertAll(List<JstackRecord> jstackList) {
    if (jstackList == null || jstackList.isEmpty()) {
      return;
    }
    mongoTemplate.insertAll(jstackList);
  }
}
