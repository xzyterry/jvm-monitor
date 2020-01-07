package com.jawnho.domain;

import com.google.common.base.Strings;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
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

  /**
   * 服务器地址
   */
  private String ip;

  /**
   * java的进程号
   */
  private String pid;

  /**
   * 服务名称
   */
  private String serviceName;

  /**
   * 线程名
   */
  private String name;

  /**
   * 线程优先级
   */
  private String prio;

  /**
   * 系统优先级
   */
  private String os_prio;

  /**
   * jvm中的线程号
   */
  private String tid;

  /**
   * 映射os中的进程号
   */
  private String nid;

  /**
   * 运行状态
   */
  private String state;

  /**
   * 线程栈帧
   */
  private String startAddress;

  /**
   * 明细
   */
  private String detail;

  /**
   * 创建时间
   */
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
