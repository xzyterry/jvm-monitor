package com.jawnho.service;

import com.jawnho.domain.GcRecord;
import java.util.List;

/**
 * @author jawnho
 * @date 2020/1/6
 */
public interface IGcRecordService {

  void insertAll(List<GcRecord> gcRecordList);
}
