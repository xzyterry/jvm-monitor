package com.jawnho.domain;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author jawnho
 * @date 2020/1/5
 */
@Document
@Setter
@Getter
public class GcRecord {

  private String dateStr;

  /**
   * 服务所在 服务器ip
   */
  private String ip;

  /**
   * 服务进程号
   */
  private String pid;

  /**
   * 服务名称
   */
  private String serviceName;

  /**
   * jstat -gc pid 结果
   */
  private List<String> data;

  @Indexed(
      background = true,
      expireAfterSeconds = 7 * 24 * 60 * 60
  )
  private Date createDate;

}
