package com.diamondcat.controller;

import com.diamondcat.config.ServiceConfig;
import com.google.common.base.Preconditions;
import com.diamondcat.param.JstackParam;
import com.google.common.base.Strings;
import com.jawnho.domain.JstackStatisticRec;
import com.jawnho.service.IJstackStatisticRecService;
import com.jawnho.util.CheckUtil;
import com.jawnho.util.LogUtil;
import com.jawnho.util.ResponseUtil;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jawnho
 * @date 2020/1/8
 */
@RestController
@RequestMapping("/api/data")
public class DataController {

  private Logger log = LoggerFactory.getLogger(DataController.class);

  private final IJstackStatisticRecService jstackStatisticRecService;

  public DataController(ServiceConfig serviceConfig) {
    Preconditions.checkNotNull(serviceConfig);
    IJstackStatisticRecService jstackStatisticRecService = serviceConfig
        .jstackStatisticRecService();
    Preconditions.checkNotNull(jstackStatisticRecService);
    this.jstackStatisticRecService = jstackStatisticRecService;
  }

  @PostMapping("/jstack")
  public Map<String, Object> jstack(@RequestBody JstackParam param) {
    CheckUtil.checkNotNull(param);
    String dateStr = param.getDateStr();
    CheckUtil.checkArgument(!Strings.isNullOrEmpty(dateStr), "dateStr is null");
    String minStr = param.getMinStr();
    CheckUtil.checkArgument(!Strings.isNullOrEmpty(minStr), "minStr is null");
    String hostName = param.getHostName();
    CheckUtil.checkArgument(!Strings.isNullOrEmpty(hostName), "hostName is null");
    String serviceName = param.getServiceName();
    CheckUtil.checkArgument(!Strings.isNullOrEmpty(serviceName), "serviceName is null");

    List<JstackStatisticRec> recList = jstackStatisticRecService.find(
        dateStr,
        minStr,
        hostName,
        serviceName
    );

    return ResponseUtil.success(null, recList);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public Map<String, Object> exceptionHandler(Exception e, Request request) {
    HttpURI uri = request.getHttpURI();
    String pathQuery = uri == null ? null : uri.getPathQuery();

    log.error(
        "DataController operation pathQuery: {} throw exception: {}",
        pathQuery,
        LogUtil.extractStackTrace(e)
    );
    return ResponseUtil.fail("异常", null);
  }
}
