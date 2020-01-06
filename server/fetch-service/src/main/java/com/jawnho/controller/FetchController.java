package com.jawnho.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.jawnho.config.ServiceConfig;
import com.jawnho.constant.CmdType;
import com.jawnho.domain.GcRecord;
import com.jawnho.domain.HostInfo;
import com.jawnho.dto.JvmOpt;
import com.jawnho.service.IGcRecordService;
import com.jawnho.service.IHostInfoService;
import com.jawnho.util.JSchExecutor;
import com.jawnho.util.LogUtil;
import com.jawnho.util.ResponseUtil;
import com.jawnho.util.TimeUtil;
import com.jcraft.jsch.JSchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jawnho
 * @date 2020/1/5
 */
@RestController
@RequestMapping("/api/fetch")
public class FetchController {

  private static Logger log = LoggerFactory.getLogger(FetchController.class);

  private final IHostInfoService hostInfoService;

  private final IGcRecordService gcRecordService;

  public FetchController(ServiceConfig serviceConfig) {
    Preconditions.checkNotNull(serviceConfig);
    IHostInfoService hostInfoService = serviceConfig.hostInfoService();
    Preconditions.checkNotNull(hostInfoService);
    this.hostInfoService = hostInfoService;

    IGcRecordService gcRecordService = serviceConfig.gcRecordService();
    Preconditions.checkNotNull(gcRecordService);
    this.gcRecordService = gcRecordService;
  }

  /**
   * 定时获取基本数据
   */
  @Scheduled(cron = "0 0/1 * * * *")
  public void fetchGc() {

    // 获取服务器连接配置
    List<HostInfo> hostInfoList = hostInfoService.findAll();

    List<GcRecord> gcRecordList = new LinkedList<>();
    for (HostInfo hostInfo : hostInfoList) {
      if (hostInfo == null) {
        continue;
      }
      // 连接主机
      JSchExecutor executor = new JSchExecutor(
          hostInfo.getUsername(),
          hostInfo.getPassword(),
          hostInfo.getHostIp()
      );
      try {
        executor.connect();
      } catch (JSchException e) {
        log.error("exec cmd fail with exception: {}", LogUtil.extractStackTrace(e));
        continue;
      }

      // 获取当前主机的所有java进程信息
      Map<String, JvmOpt> jvmOptMap = getJvmOpt(executor);
      for (Entry<String, JvmOpt> entry : jvmOptMap.entrySet()) {
        if (entry == null) {
          continue;
        }

        String pid = entry.getKey();
        if (Strings.isNullOrEmpty(pid)) {
          continue;
        }

        JvmOpt jvmOpt = entry.getValue();
        if (jvmOpt == null) {
          continue;
        }

        // 生成gcRecord
        GcRecord gcRecord = getGcRecord(executor, hostInfo, jvmOpt);
        if (gcRecord == null) {
          continue;
        }
        gcRecordList.add(gcRecord);
      }

      // 入库
      gcRecordService.insertAll(gcRecordList);

    }
  }

  /**
   * 获取 jstat -gc pid 结果
   */
  private GcRecord getGcRecord(
      JSchExecutor executor,
      HostInfo hostInfo,
      JvmOpt jvmOpt) {
    Preconditions.checkNotNull(executor);
    Preconditions.checkNotNull(hostInfo);
    Preconditions.checkNotNull(jvmOpt);

    String gcCmd = CmdType.J_STAT + " " + CmdType.GC + " " + jvmOpt.getPid();
    List<String> retMsgList;
    try {
      retMsgList = executor.execCmd(gcCmd);
    } catch (Exception e) {
      log.error("exec cmd fail with exception: {}", LogUtil.extractStackTrace(e));
      return null;
    }

    List<String> data = parseGcRetMsg(retMsgList);
    if (data == null) {
      return null;
    }

    GcRecord rec = new GcRecord();
    rec.setDateStr(TimeUtil.getCurDateStr());
    rec.setIp(hostInfo.getHostIp());
    rec.setPid(jvmOpt.getPid());
    rec.setServiceName(jvmOpt.getServiceName());
    rec.setData(data);
    rec.setCreateDate(TimeUtil.getCurDate());

    return rec;
  }

  private Map<String, JvmOpt> getJvmOpt(JSchExecutor executor) {
    Preconditions.checkNotNull(executor);

    String psCmd = "ps -ef|grep java";
    List<String> result = null;
    try {
      result = executor.execCmd(psCmd);
    } catch (Exception e) {
      log.info("ps cmd fail with exception: {}", LogUtil.extractStackTrace(e));
    }

    Map<String, JvmOpt> jvmOptMap = Maps.newHashMap();
    for (String line : result) {
      if (Strings.isNullOrEmpty(line)) {
        continue;
      }

      JvmOpt jvmOpt = new JvmOpt();
      String pid = getPid(line);
      if (Strings.isNullOrEmpty(pid)) {
        continue;
      }

      String serviceName = getServiceName(line);
      if (Strings.isNullOrEmpty(serviceName)) {
        continue;
      }

      jvmOpt.setPid(pid);
      jvmOpt.setServiceName(serviceName);
      jvmOpt.setXmx(getXmx(line));

      jvmOptMap.put(pid, jvmOpt);
    }

    return jvmOptMap;
  }

  /**
   * 获取pid
   */
  private String getPid(String line) {
    if (Strings.isNullOrEmpty(line)) {
      return null;
    }

    String[] split = line.split(" ");
    int end = 0;
    for (String s : split) {
      if (Strings.isNullOrEmpty(s) || " ".equals(s)) {
        continue;
      }

      end++;
      if (end == 2) {
        return s;
      }
    }

    return null;
  }

  /**
   * 获取最大堆大小
   */
  private String getXmx(String line) {
    if (Strings.isNullOrEmpty(line)) {
      return null;
    }

    String xmx = "";
    for (int i = line.indexOf("-Xmx"); i < line.length(); i++) {
      char c = line.charAt(i);
      if (c != ' ') {
        xmx += c;
      } else {
        break;
      }
    }
    return xmx;
  }

  /**
   * 获取服务名称
   */
  private String getServiceName(String line) {
    if (Strings.isNullOrEmpty(line)) {
      return null;
    }

    int start = line.indexOf("-classpath");
    if (start == -1) {
      return null;
    }

    String subLine = line.substring(start);
    int end = subLine.indexOf(":");
    if (end == -1) {
      return null;
    }

    String classPathStr = subLine.substring(0, end);
    String[] split = classPathStr.split("/");
    String jarStr = split[split.length - 1];
    int suffixStart = jarStr.indexOf(".");
    if (suffixStart == -1) {
      return null;
    }
    return jarStr.substring(0, suffixStart);
  }

  /**
   * 解析返回信息
   */
  private List<String> parseGcRetMsg(List<String> retMsgList) {
    if (retMsgList == null) {
      return null;
    }

    if (retMsgList.size() != 2) {
      return null;
    }

    List<String> result = new LinkedList<>();
    String retMsg = retMsgList.get(1);
    String[] splitArr = retMsg.split(" ");
    for (String split : splitArr) {
      if (Strings.isNullOrEmpty(split) || " ".equals(split)) {
        continue;
      }

      result.add(split);
    }

    return result;
  }


  @ExceptionHandler(Exception.class)
  @ResponseBody
  public Map<String, Object> exceptionHandler(Exception e, Request request) {

    HttpURI uri = request.getHttpURI();
    String pathQuery = uri == null ? null : uri.getPathQuery();

    log.error(
        "FetchController operation pathQuery: {} throw exception: {}",
        pathQuery,
        LogUtil.extractStackTrace(e)
    );
    return ResponseUtil.fail("异常", null);
  }

}
