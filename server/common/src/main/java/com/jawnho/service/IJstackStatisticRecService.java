package com.jawnho.service;

import com.jawnho.domain.JstackStatisticRec;
import java.util.List;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public interface IJstackStatisticRecService {

  void insertAll(List<JstackStatisticRec> recList);

  List<JstackStatisticRec> find(String dateStr, String minStr, String hostName, String serviceName);
}
