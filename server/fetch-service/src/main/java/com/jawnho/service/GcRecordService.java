package com.jawnho.service;

import com.google.common.base.Preconditions;
import com.jawnho.domain.GcRecord;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author jawnho
 * @date 2020/1/6
 */
public class GcRecordService implements IGcRecordService {

  private final MongoTemplate mongoTemplate;

  public GcRecordService(MongoTemplate mainMongoTemplate) {
    Preconditions.checkNotNull(mainMongoTemplate);
    this.mongoTemplate = mainMongoTemplate;
  }

  @Override
  public void insertAll(List<GcRecord> gcRecordList) {
    Preconditions.checkNotNull(gcRecordList);
    mongoTemplate.insertAll(gcRecordList);
  }
}
