package com.jawnho.service;

import com.jawnho.domain.JstackRecord;
import java.util.List;

/**
 * @author jawnho
 * @date 2020/1/8
 */
public interface IJstackRecordService {

  void insertAll(List<JstackRecord> jstackList);
}
