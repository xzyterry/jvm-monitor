package com.jawnho.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * jstat -gc 返回信息
 *
 * @author jawnho
 * @date 2020/1/5
 */
@Setter
@Getter
public class GcField {

  /**
   * 年轻代s0容量
   */
  private String s0c;

  /**
   * 年轻代s0使用
   */
  private String s0u;

  /**
   * 年轻代s1容量
   */
  private String s1c;

  /**
   * 年轻代s1使用
   */
  private String s1u;

  /**
   * 年轻代eden容量
   */
  private String ec;

  /**
   * 年轻代eden使用
   */
  private String eu;

  /**
   * 老年代容量
   */
  private String oc;

  /**
   * 老年代使用
   */
  private String ou;

  /**
   * metaspace容量
   */
  private String mc;

  /**
   * metaspace使用
   */
  private String mu;

  /**
   * 启动到当前 年轻代gc次数
   */
  private String ygc;

  /**
   * 启动到当前 年轻代gc所用的时间 s
   */
  private String ygct;

  /**
   * 启动到当前 full gc次数
   */
  private String fgc;


  /**
   * 启动到当前 full gc所用时间 s
   */
  private String fgct;

  /**
   * 启动到当前 gc总用时 s
   */
  private String gct;

}
