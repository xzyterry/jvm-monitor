package com.jawnho.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jawnho.domain.HostService;
import com.jawnho.util.TimeUtil;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public class HostServiceService implements IHostServiceService {

  private final MongoTemplate mongoTemplate;

  public HostServiceService(MongoTemplate mainMongoTemplate) {
    Preconditions.checkNotNull(mainMongoTemplate);
    this.mongoTemplate = mainMongoTemplate;
  }

  @Override
  public void insertAll(List<HostService> hostServiceList) {
    Preconditions.checkNotNull(hostServiceList);

    for (HostService hostService : hostServiceList) {
      if (hostService == null) {
        continue;
      }

      String hostName = hostService.getHostName();
      if (Strings.isNullOrEmpty(hostName)) {
        continue;
      }

      String serviceName = hostService.getServiceName();
      if (Strings.isNullOrEmpty(serviceName)) {
        continue;
      }

      Query query = new Query(
          Criteria.where(HostService.HOST_NAME).is(hostName)
              .and(HostService.SERVICE_NAME).is(serviceName)
      );

      Date curDate = TimeUtil.getCurDate();
      Update update = new Update();
      update.setOnInsert(HostService.SERVICE_NAME, serviceName);
      update.setOnInsert(HostService.HOST_NAME, hostName);
      update.setOnInsert(HostService.CREATE_DATE, curDate);
      update.set(HostService.UPDATE_DATE, curDate);

      FindAndModifyOptions options = new FindAndModifyOptions();
      options.upsert(true);
      options.returnNew(true);

      mongoTemplate.findAndModify(query, update, options, HostService.class);
    }
  }

  @Override
  public List<HostService> findAll() {
    return mongoTemplate.findAll(HostService.class);
  }
}
