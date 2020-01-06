package com.jawnho.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jawnho
 * @date 2020/1/5
 */
@Setter
@Getter
public class JvmOpt {

  /**
   * 进程号
   */
  private String pid;

  /**
   * 服务名称
   */
  private String serviceName;

  /**
   * 最大堆内存
   */
  private String xmx;

}
