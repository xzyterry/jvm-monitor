package com.jawnho.domain;

import com.google.common.base.Strings;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jawnho
 * @date 2020/1/6
 */
@Document
@Setter
@Getter
public class JstackRecord {

  /**
   * 日期
   */
  private String dateStr;

  public static final String DATE_STR = "dateStr";

  /**
   * 整分钟
   */
  private String minStr;

  public static final String MIN_STR = "minStr";

  /**
   * 服务器地址
   */
  private String ip;

  /**
   * 服务器自定义名称
   */
  private String hostName;

  public static final String HOST_NAME = "hostName";

  /**
   * java的进程号
   */
  private String pid;

  /**
   * 服务名称
   */
  private String serviceName;

  public static final String SERVICE_NAME = "serviceName";

  /**
   * 线程名
   */
  private String name;

  /**
   * 运行状态
   */
  private String state;

  public static final String STATE ="state";

  /**
   * 明细
   */
  private String detail;

  public static final String DETAIL = "detail";

  /**
   * 创建时间
   */
  @Indexed(
      background = true,
      expireAfterSeconds = 1 * 24 * 60 * 60
  )
  private Date createDate;

  public void cal() {
    int start = 0;
    if (Strings.isNullOrEmpty(this.detail)) {
      return;
    }

    start = this.detail.indexOf("java.lang.Thread.State: ");
    if (start < 0) {
      return;
    }

    start += "java.lang.Thread.State: ".length();

    String substring = this.detail.substring(start);
    int end = substring.indexOf("\n");
    if (end < 0) {
      return;
    }
    this.setState(substring.substring(0, end ).trim());
  }

}
