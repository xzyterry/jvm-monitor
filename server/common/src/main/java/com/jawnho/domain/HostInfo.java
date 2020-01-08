package com.jawnho.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jawnho
 * @date 2020/1/5
 */
@Document
@Setter
@Getter
public class HostInfo {

  /**
   * 主机ip
   */
  private String hostIp;

  /**
   * 主机昵称(自定义)
   */
  private String hostName;

  private String username;

  private String password;

}
