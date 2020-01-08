package com.jawnho.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jawnho.domain.JstackStatisticRec;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public class JstackStatisticRecService implements IJstackStatisticRecService {

  private final MongoTemplate mongoTemplate;

  public JstackStatisticRecService(MongoTemplate mainMongoTemplate) {
    Preconditions.checkNotNull(mainMongoTemplate);
    this.mongoTemplate = mainMongoTemplate;
  }

  @Override
  public void insertAll(List<JstackStatisticRec> recList) {
    Preconditions.checkNotNull(recList);

    mongoTemplate.insertAll(recList);
  }

  @Override
  public List<JstackStatisticRec> find(
      String dateStr,
      String minStr,
      String hostName,
      String serviceName) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateStr));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(minStr));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(hostName));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(serviceName));

    Query query = new Query(
        Criteria.where(JstackStatisticRec.DATE_STR).is(dateStr)
            .and(JstackStatisticRec.MIN_STR).is(minStr)
            .and(JstackStatisticRec.HOST_NAME).is(hostName)
            .and(JstackStatisticRec.SERVICE_NAME).is(serviceName)
    );

    return mongoTemplate.find(query, JstackStatisticRec.class);
  }
}
