package com.diamondcat.controller;

import com.diamondcat.config.ServiceConfig;
import com.google.common.base.Preconditions;
import com.jawnho.domain.JstackStatisticRec;
import com.jawnho.service.IJstackRecordService;
import com.jawnho.service.IJstackStatisticRecService;
import com.jawnho.util.TimeUtil;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jawnho
 * @date 2020/1/8
 */
@RestController
@RequestMapping("/api")
public class ScheduleController {

  private Logger log = LoggerFactory.getLogger(ScheduleController.class);

  private IJstackRecordService jstackRecordService;

  private IJstackStatisticRecService jstackStatisticRecService;

  public ScheduleController(ServiceConfig serviceConfig) {
    Preconditions.checkNotNull(serviceConfig);

    IJstackRecordService jstackRecordService = serviceConfig.jstackRecordService();
    Preconditions.checkNotNull(jstackRecordService);
    this.jstackRecordService = jstackRecordService;

    IJstackStatisticRecService jstackStatisticRecService = serviceConfig
        .jstackStatisticRecService();
    Preconditions.checkNotNull(jstackStatisticRecService);
    this.jstackStatisticRecService = jstackStatisticRecService;
  }

  /**
   * jstack
   */
  @Scheduled(cron = "0 0/1 * * * *")
  public void jstack() {
    Date date = TimeUtil.getCurDate();
    String dateMinuteStr = TimeUtil.getDateMinuteStr(date, 2);
    log.info("jstack start at: {} , execute {} task",
        TimeUtil.specialFormatToDateStr(date),
        dateMinuteStr
    );

    String dateStr = TimeUtil.getCurDateStr();
    List<JstackStatisticRec> recList = jstackRecordService.aggregate(dateMinuteStr);
    if (recList != null && !recList.isEmpty()) {

      for (JstackStatisticRec rec : recList) {
        if (rec == null) {
          continue;
        }

        rec.setDateStr(dateStr);
        rec.setMinStr(dateMinuteStr);
        rec.setCreateDate(date);
      }
      jstackStatisticRecService.insertAll(recList);
    }

    date = TimeUtil.getCurDate();
    log.info("jstack end at: {} , execute {} task",
        TimeUtil.specialFormatToDateStr(date),
        dateMinuteStr
    );
  }

}
