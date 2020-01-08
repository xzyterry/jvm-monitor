package com.jawnho.service;

import com.jawnho.domain.JstackRecord;
import com.jawnho.domain.JstackStatisticRec;
import java.util.List;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public interface IJstackRecordService {

  void insertAll(List<JstackRecord> jstackList);

  List<JstackStatisticRec> aggregate(String dateMinuteStr);

}
