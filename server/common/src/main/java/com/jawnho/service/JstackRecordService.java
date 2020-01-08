package com.jawnho.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jawnho.domain.JstackRecord;
import com.jawnho.domain.JstackStatisticRec;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

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

  @Override
  public List<JstackStatisticRec> aggregate(String dateMinuteStr) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dateMinuteStr));

    //  hostName serviceName state

    GroupOperation group = Aggregation.group(
        JstackRecord.HOST_NAME,
        JstackRecord.SERVICE_NAME,
        JstackRecord.STATE
    ).count().as(JstackStatisticRec.COUNT);

    MatchOperation match = Aggregation
        .match(Criteria.where(JstackRecord.MIN_STR).is(dateMinuteStr));
    AggregationResults<JstackStatisticRec> aggregate = mongoTemplate.aggregate(
        Aggregation.newAggregation(match, group),
        JstackRecord.class,
        JstackStatisticRec.class
    );

    return aggregate.getMappedResults();
  }
}
